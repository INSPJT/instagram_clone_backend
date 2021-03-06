package our.yurivongella.instagramclone.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import our.yurivongella.instagramclone.controller.dto.CommentCreateDto;
import our.yurivongella.instagramclone.controller.dto.SignupRequestDto;
import our.yurivongella.instagramclone.controller.dto.comment.ProcessStatus;
import our.yurivongella.instagramclone.controller.dto.post.CommentResponseDto;
import our.yurivongella.instagramclone.domain.comment.Comment;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.member.MemberRepository;
import our.yurivongella.instagramclone.domain.post.Post;
import our.yurivongella.instagramclone.domain.post.PostRepository;

@Transactional
@SpringBootTest
class CommentServiceTest {
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AuthService authService;

    private Long userId;

    @BeforeEach
    public void signupBeforeTest() {
        String name = "testName";
        String nickName = "testNick";
        String email = "test@naver.com";
        String password = "testPassword";
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


        Post post = Post.builder()
                        .content("test")
                        .build();
        Member member = memberRepository.findById(userId).get();
        post.addMember(member);
        Post save = postRepository.save(post);
        assertNotNull(save);
    }

    @Test
    @DisplayName("댓글 달기")
    public void create_comment() throws Exception {
        CommentCreateDto commentCreateDto = new CommentCreateDto("test");
        CommentResponseDto comment = commentService.createComment(1L, commentCreateDto);

        assertEquals("test",comment.getContent());
        assertEquals("testName",comment.getAuthor().getName());
        assertEquals(0,comment.getLikeCount());
        assertFalse(comment.getIsLike());
    }

    @DisplayName("댓글 삭제")
    @Test
    public void delete_comment() throws Exception {
        CommentCreateDto commentCreateDto = new CommentCreateDto("test");
        CommentResponseDto comment = commentService.createComment(1L, commentCreateDto);

        assertEquals("test",comment.getContent());
        assertEquals("testName",comment.getAuthor().getName());
        assertEquals(0,comment.getLikeCount());
        assertFalse(comment.getIsLike());

        ProcessStatus processStatus = commentService.deleteComment(1L);

        assertEquals(ProcessStatus.SUCCESS ,processStatus);
    }


    @Test
    @DisplayName("댓글 불러오기")
    public void getComment_test() throws Exception {
        List<CommentResponseDto> comments = commentService.getComments(1L);
        assertEquals("test",comments.get(0).getContent());
        assertEquals(1,comments.size());
    }

    @DisplayName("댓글 좋아요")
    @Test
    public void like() throws Exception {

    }

    @DisplayName("댓글 좋아요 취소")
    @Test
    public void unlike() throws Exception {

    }




}