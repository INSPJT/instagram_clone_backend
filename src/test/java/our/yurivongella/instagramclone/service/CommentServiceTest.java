package our.yurivongella.instagramclone.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
import our.yurivongella.instagramclone.exception.CustomException;
import our.yurivongella.instagramclone.exception.ErrorCode;

import static org.junit.jupiter.api.Assertions.*;

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
        String displayId = "testName";
        String nickname = "testNick";
        String email = "test@naver.com";
        String password = "testPassword";
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                                                            .displayId(displayId)
                                                            .nickname(nickname)
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
        assertEquals("testName", comment.getAuthor().getDisplayId());
        assertEquals(0, comment.getLikeCount());
        assertFalse(comment.getIsLike());
    }

    @DisplayName("본인이 본인 댓글 삭제")
    @Test
    public void delete_comment1() throws Exception {
        ProcessStatus processStatus = commentService.deleteComment(commentId);
        Optional<Comment> comment = commentRepository.findById(commentId);
        assertEquals(Optional.empty(), comment);
        assertEquals(ProcessStatus.SUCCESS, processStatus);
    }

    @DisplayName("타인이 타인 댓글 삭제")
    @Test
    public void delete_comment2() throws Exception {
        String displayId = "other";
        String nickname = "other";
        String email = "other@naver.com";
        String password = "other";
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                                                            .displayId(displayId)
                                                            .nickname(nickname)
                                                            .email(email)
                                                            .password(password)
                                                            .build();

        // 가입
        authService.signup(signupRequestDto);
        Long otherId = memberRepository.findByEmail(email).get().getId();

        Authentication authentication = new UsernamePasswordAuthenticationToken(otherId, "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        assertEquals(ProcessStatus.FAIL, commentService.deleteComment(commentId));
        Optional<Comment> comment = commentRepository.findById(commentId);
        assertTrue(comment.isPresent());
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

        CustomException customException = assertThrows(CustomException.class, () -> commentService.likeComment(commentId));
        assertEquals(ErrorCode.ALREADY_LIKE, customException.getErrorCode()); // 중복 처리
    }

    @DisplayName("댓글 좋아요 취소")
    @Test
    public void unlike() throws Exception {
        ProcessStatus processStatus = commentService.likeComment(commentId);
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        assertEquals(ProcessStatus.SUCCESS, processStatus);
        assertEquals(1, comment.getCommentLikes().size());

        processStatus = commentService.unlikeComment(commentId);
        comment = commentRepository.findById(commentId).orElseThrow();
        assertEquals(ProcessStatus.SUCCESS, processStatus);
        assertEquals(0, comment.getCommentLikes().size());

        CustomException customException = assertThrows(CustomException.class, () -> commentService.unlikeComment(commentId));
        assertEquals(ErrorCode.ALREADY_UNLIKE, customException.getErrorCode()); // 중복 처리
    }

}