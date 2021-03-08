package our.yurivongella.instagramclone.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import our.yurivongella.instagramclone.controller.dto.SignupRequestDto;
import our.yurivongella.instagramclone.controller.dto.post.PostCreateRequestDto;
import our.yurivongella.instagramclone.controller.dto.post.PostReadResponseDto;
import our.yurivongella.instagramclone.domain.member.MemberRepository;
import our.yurivongella.instagramclone.domain.post.Post;
import our.yurivongella.instagramclone.domain.post.PostRepository;
import our.yurivongella.instagramclone.util.SecurityUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthService authService;

    private Long userId;
    private static PostCreateRequestDto postCreateRequestDto;

    private final String name = "test";
    private final String nickName = "testNickname";
    private final String email = "authService1@test.net";
    private final String password = "1q2w3e4r";

    @BeforeEach
    public void signupBeforeTest() {
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .name(name)
                .nickName(nickName)
                .email(email)
                .password(password)
                .build();

        // 가입
        authService.signup(signupRequestDto);
        userId = memberRepository.findByEmail(email).get().getId();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userId, "", Collections.emptyList());
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

        List<Post> list = postRepository.findAll();

        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getMember().getId()).isEqualTo(userId);
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

        assertThat(postResponseDto.getAuthor().getId()).isEqualTo(userId);
        assertThat(postResponseDto.getContent()).isEqualTo(postCreateRequestDto.getContent());

        for (int i = 0; i < postCreateRequestDto.getMediaUrls().size(); ++i) {
            assertThat(postResponseDto.getMediaUrls().get(i)).isEqualTo(postCreateRequestDto.getMediaUrls().get(i));
        }

        assertThat(postResponseDto.getLikeCount()).isEqualTo(0);
        assertThat(postResponseDto.getCommentCount()).isEqualTo(0);
    }

    @DisplayName("게시물 삭제")
    @Test
    public void DeleteOnePost() {
        Long postId = postService.create(postCreateRequestDto);

        System.out.println("Request UserId : " + SecurityUtil.getCurrentMemberId());

        postService.delete(postId);

        Assertions.assertThrows(
                RuntimeException.class,
                () -> postService.read(postId)
        );
    }
}
