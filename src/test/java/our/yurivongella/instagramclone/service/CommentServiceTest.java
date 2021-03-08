package our.yurivongella.instagramclone.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

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
import our.yurivongella.instagramclone.domain.comment.CommentRepository;
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
    private CommentRepository commentRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AuthService authService;

    @Autowired
    private EntityManager em;

    private Long userId;
    private Long postId;
    private Long commentId;

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
        postId = save.getId();
        assertNotNull(save);

        CommentCreateDto commentCreateDto = new CommentCreateDto("test");
        CommentResponseDto comment = commentService.createComment(postId, commentCreateDto);
        commentId = save.getComments().get(0).getId();
        assertNotNull(comment);
    }

    @Test
    @DisplayName("댓글 달기")
    public void create_comment() throws Exception {
        CommentCreateDto commentCreateDto = new CommentCreateDto("test");
        CommentResponseDto comment = commentService.createComment(postId, commentCreateDto);

        assertEquals("test", comment.getContent());
        assertEquals("testName", comment.getAuthor().getName());
        assertEquals(0, comment.getLikeCount());
        assertFalse(comment.getIsLike());
    }

    @DisplayName("본인이 본인 댓글 삭제")
    @Test
    public void delete_comment1() throws Exception {
        ProcessStatus processStatus = commentService.deleteComment(commentId);
        List<Comment> all = commentRepository.findAll();
        assertEquals(0, all.size());
        assertEquals(ProcessStatus.SUCCESS, processStatus);
    }

    @DisplayName("타인이 타인 댓글 삭제")
    @Test
    public void delete_comment2() throws Exception {
        String name = "other";
        String nickName = "other";
        String email = "other@naver.com";
        String password = "other";
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                                                            .name(name)
                                                            .nickName(nickName)
                                                            .email(email)
                                                            .password(password)
                                                            .build();

        // 가입
        authService.signup(signupRequestDto);
        Long otherId = memberRepository.findByEmail(email).get().getId();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(otherId, "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        assertEquals(ProcessStatus.FAIL, commentService.deleteComment(commentId));

        List<Comment> all = commentRepository.findAll();
        assertEquals(1, all.size());
    }

    @Test
    @DisplayName("댓글 불러오기")
    public void getComment_test() throws Exception {
        List<CommentResponseDto> comments = commentService.getComments(postId);
        assertEquals("test", comments.get(0).getContent());
        assertEquals(1, comments.size());
    }

    @DisplayName("댓글 좋아요")
    @Test
    public void like() throws Exception {
        ProcessStatus processStatus = commentService.likeComment(commentId);
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        assertEquals(ProcessStatus.SUCCESS, processStatus);
        assertEquals(1, comment.getCommentLikes().size());

        processStatus = commentService.likeComment(commentId); // 한번 더 하면
        assertEquals(ProcessStatus.FAIL, processStatus); // 실패
        assertEquals(1, comment.getCommentLikes().size()); // 좋아요는 그대로
    }

    @DisplayName("댓글 좋아요 취소")
    @Test
    public void unlike() throws Exception {
        ProcessStatus processStatus = commentService.likeComment(commentId); //일단 좋아하고
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        assertEquals(ProcessStatus.SUCCESS, processStatus);
        assertEquals(1, comment.getCommentLikes().size());

        processStatus = commentService.unlikeComment(commentId); // 좋아요 취소
        comment = commentRepository.findById(commentId).orElseThrow();
        assertEquals(ProcessStatus.SUCCESS, processStatus);
        assertEquals(0, comment.getCommentLikes().size());

        processStatus = commentService.unlikeComment(commentId); // 한번 더 해도
        assertEquals(ProcessStatus.FAIL, processStatus); // 실패
        assertEquals(0, comment.getCommentLikes().size()); // 좋아요 그대로
    }

}