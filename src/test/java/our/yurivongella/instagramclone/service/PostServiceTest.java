package our.yurivongella.instagramclone.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import our.yurivongella.instagramclone.controller.dto.auth.SignupReqDto;
import our.yurivongella.instagramclone.controller.dto.comment.ProcessStatus;
import our.yurivongella.instagramclone.controller.dto.post.PostCreateRequestDto;
import our.yurivongella.instagramclone.controller.dto.post.PostReadResponseDto;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.member.MemberRepository;
import our.yurivongella.instagramclone.domain.post.Post;
import our.yurivongella.instagramclone.domain.post.PostRepository;
import our.yurivongella.instagramclone.exception.CustomException;
import our.yurivongella.instagramclone.exception.ErrorCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
public class PostServiceTest {
    @MockBean
    private S3Service s3Service;

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthService authService;

    private Long memberId;
    private static PostCreateRequestDto postCreateRequestDto;

    private final String displayId = "test";
    private final String nickname = "testNickname";
    private final String email = "authService1@test.net";
    private final String password = "1q2w3e4r";

    @BeforeEach
    public void signupBeforeTest() {
        SignupReqDto signupReqDto = SignupReqDto.builder()
                                                            .displayId(displayId)
                                                            .nickname(nickname)
                                                            .email(email)
                                                            .password(password)
                                                            .build();

        // 가입
        authService.signup(signupReqDto);
        memberId = memberRepository.findByEmail(email).get().getId();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(memberId, "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @BeforeEach
    public void generatePostRequestDto() {
        // set mediaUrls mock PostRequestDto
        List<String> mediaUrls = new ArrayList<>();
        mediaUrls.add("mock url 1");
        mediaUrls.add("mock url 2");
        mediaUrls.add("mock url 3");
        // make mock PostRequestDto
        PostCreateRequestDto postCreateRequestDto = new PostCreateRequestDto(mediaUrls, "시험중인 글 입니다.");
        this.postCreateRequestDto = postCreateRequestDto;
    }

    @DisplayName("게시글 생성")
    @Test
    public void createPost() {
        // request mock Post
        postService.create(postCreateRequestDto);
        Member member = memberRepository.findById(memberId).get();

        List<Post> list = postRepository.findAllByMember(member);

        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getMember().getId()).isEqualTo(memberId);
        assertThat(list.get(0).getMember().getPostCount()).isEqualTo(1);
        assertThat(list.get(0).getContent()).isEqualTo(postCreateRequestDto.getContent());
        assertThat(list.get(0).getMediaUrls().size()).isEqualTo(3);
    }

    @DisplayName("게시글 한개 읽기")
    @Test
    public void readOnePost() {
        // request mock Post
        Long postId = postService.create(postCreateRequestDto);

        // reqeust mock get
        PostReadResponseDto postResponseDto = postService.read(postId);

        assertThat(postResponseDto.getAuthor().getDisplayId()).isEqualTo(displayId);
        assertThat(postResponseDto.getContent()).isEqualTo(postCreateRequestDto.getContent());

        for (int i = 0; i < postCreateRequestDto.getMediaUrls().size(); ++i) {
            assertThat(postResponseDto.getMediaUrls().get(i)).isEqualTo(postCreateRequestDto.getMediaUrls().get(i));
        }

        assertThat(postResponseDto.getLikeCount()).isEqualTo(0);
        assertThat(postResponseDto.getCommentCount()).isEqualTo(0);
    }

    @DisplayName("게시물 삭제")
    @Test
    public void deleteOnePost() {
        // given
        Long postId = postService.create(postCreateRequestDto);
        Post post = postRepository.findById(postId).get();
        assertThat(post.getMember().getPostCount()).isEqualTo(1);

        // when
        postService.delete(postId);

        // then
        assertThat(post.getMember().getPostCount()).isEqualTo(0);
        Assertions.assertThrows(
                RuntimeException.class,
                () -> postService.read(postId)
        );
    }

    @DisplayName("특정 유저의 게시글 피드 가져오기")
    @Test
    public void getFeeds() {
        // given
        Member member = memberRepository.findByEmail("woody@test.net").get();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        int lastPostId = 1;
        int pageSize = 5;
        PageRequest pageRequest = PageRequest.of(lastPostId, pageSize);
        // when
        List<PostReadResponseDto> feeds = postService.getFeeds(pageRequest).getContent();

        // then
        assertThat(feeds.size()).isEqualTo(pageSize);
        feeds.forEach(feed -> assertThat(feed.getId()).isGreaterThan(lastPostId));
    }

    @DisplayName("특정 유저의 게시글 피드 가져오기")
    @Test
    public void getFeedsNoLastPostId() {
        // given
        Member member = memberRepository.findByEmail("woody@test.net").get();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        int pageSize = 5;
        PageRequest pageRequest = PageRequest.of(0, pageSize);
        // when
        List<PostReadResponseDto> feeds = postService.getFeeds(pageRequest).getContent();
        // then
        assertThat(feeds.size()).isEqualTo(pageSize);
    }

    @DisplayName("좋아요 테스트")
    @Test
    @Transactional
    public void likeTest() {
        Long postId = postService.create(postCreateRequestDto);
        ProcessStatus processStatus = postService.likePost(postId);
        assertEquals(ProcessStatus.SUCCESS, processStatus);

        Post post = postRepository.findById(postId).get();
        assertEquals(1L, post.getLikeCount());
        assertEquals(memberId, post.getPostLikes().get(0).getMember().getId());
        assertEquals(displayId, post.getPostLikes().get(0).getMember().getDisplayId());
    }

    @DisplayName("좋아요 중복 테스트")
    @Test
    @Transactional
    public void like_duple_Test() {
        Long postId = postService.create(postCreateRequestDto);
        ProcessStatus processStatus = postService.likePost(postId);
        assertEquals(ProcessStatus.SUCCESS, processStatus);

        Post post = postRepository.findById(postId).get();
        assertEquals(1L, post.getLikeCount());
        assertEquals(memberId, post.getPostLikes().get(0).getMember().getId());
        assertEquals(displayId, post.getPostLikes().get(0).getMember().getDisplayId());

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> postService.likePost(postId));
        assertEquals(ErrorCode.ALREADY_LIKE, customException.getErrorCode());
    }

    @DisplayName("취소 테스트")
    @Nested
    class WithdrawalClass {
        private Long postId;

        @BeforeEach
        public void likePost() {
            postId = postService.create(postCreateRequestDto);
            postService.likePost(postId);
            Post post = postRepository.findById(postId).get();
            assertEquals(1L, post.getLikeCount());
        }

        @DisplayName("좋아요 취소 테스트")
        @Test
        public void unlike_Test() {
            ProcessStatus processStatus = postService.unlikePost(postId);
            Post post = postRepository.findById(postId).get();
            assertEquals(ProcessStatus.SUCCESS, processStatus);
            assertEquals(0L, post.getLikeCount());
        }

        @DisplayName("좋아요 취소 중복 테스트")
        @Test
        public void unlike_duple_Test() {
            ProcessStatus processStatus = postService.unlikePost(postId);
            Post post = postRepository.findById(postId).get();
            assertEquals(ProcessStatus.SUCCESS, processStatus);
            assertEquals(0L, post.getLikeCount());

            CustomException customException = Assertions.assertThrows(CustomException.class, () -> postService.unlikePost(postId));
            assertEquals(ErrorCode.ALREADY_UNLIKE, customException.getErrorCode());
        }
    }

}
