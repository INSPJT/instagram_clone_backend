package our.yurivongella.instagramclone.service;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import our.yurivongella.instagramclone.controller.dto.comment.CommentReqDto;
import our.yurivongella.instagramclone.controller.dto.comment.CommentResDto;
import our.yurivongella.instagramclone.controller.dto.member.SignupReqDto;
import our.yurivongella.instagramclone.controller.dto.ProcessStatus;
import our.yurivongella.instagramclone.controller.dto.comment.CommentDto;
import our.yurivongella.instagramclone.domain.comment.Comment;
import our.yurivongella.instagramclone.domain.comment.CommentRepository;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.member.MemberRepository;
import our.yurivongella.instagramclone.domain.post.Post;
import our.yurivongella.instagramclone.domain.post.PostRepository;
import our.yurivongella.instagramclone.exception.CustomException;
import our.yurivongella.instagramclone.exception.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class CommentServiceTest {
    @MockBean
    private S3Service s3Service;

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

    private Long userId;
    private Long postId;
    private Long commentId;

    String displayId = "testName";
    String nickname = "testNick";
    String email = "test@naver.com";
    String password = "testPassword";

    @BeforeEach
    public void prepare_member_and_post() {
        SignupReqDto signupReqDto = SignupReqDto.builder()
                                                .displayId(displayId)
                                                .nickname(nickname)
                                                .email(email)
                                                .password(password)
                                                .build();

        // 가입
        authService.signup(signupReqDto);
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
    }

    @Nested
    @DisplayName("댓글이 존재하지 않을 때의 테스트")
    class CommentTest1 {

        private Long notExistCommendId = 123456L;

        @Test
        @DisplayName("댓글 달기")
        void create_comment() {
            CommentReqDto commentReqDto = new CommentReqDto("test");
            CommentDto comment = commentService.createComment(postId, commentReqDto);

            assertEquals("test", comment.getContent());
            assertEquals("testName", comment.getAuthor().getDisplayId());
            assertEquals(0, comment.getLikeCount());
            assertFalse(comment.getIsLike());

            Comment savedComment = commentRepository.findById(comment.getId()).get();
            assertEquals(1, savedComment.getPost().getCommentCount());
        }

        @DisplayName("존재하지 않는 댓글 삭제")
        @Test
        void delete_comment1() {
            CustomException customException = assertThrows(CustomException.class, () -> commentService.deleteComment(notExistCommendId));
            Assertions.assertEquals(ErrorCode.COMMENT_NOT_FOUND, customException.getErrorCode());
        }

        @Test
        @DisplayName("댓글 불러오기")
        void getComment_test() {
            CommentResDto commentResDto =  commentService.getCommentsFromPost(postId, null);
            assertEquals(0, commentResDto.getCommentResDtos().size());
        }

        @DisplayName("존재하지 않는 댓글 좋아요")
        @Test
        void like() {
            CustomException customException = assertThrows(CustomException.class, () -> commentService.likeComment(notExistCommendId));
            assertEquals(ErrorCode.COMMENT_NOT_FOUND, customException.getErrorCode());
        }

        @DisplayName("존재하지 않는 댓글 좋아요 취소")
        @Test
        void unlike() {
            CustomException customException = assertThrows(CustomException.class, () -> commentService.unlikeComment(notExistCommendId));
            assertEquals(ErrorCode.COMMENT_NOT_FOUND, customException.getErrorCode());

        }
    }

    @Nested
    @DisplayName("댓글이 존재할 때의 테스트")
    class CommentIs {
        @BeforeEach
        public void prepare_data() {// 댓글 불러오기, 삭제/좋아요 취소 테스트를 위해 댓글 1건을 미리 준비합니다.
            CommentReqDto commentReqDto = new CommentReqDto("test");
            Post post = postRepository.findById(postId).get();
            CommentDto comment = commentService.createComment(postId, commentReqDto);
            commentId = post.getComments().get(0).getId();
            assertNotNull(comment);
        }

        @DisplayName("본인이 본인 댓글 삭제")
        @Test
        void delete_comment1() {
            Comment deletedComment = commentRepository.findById(commentId).get();
            assertEquals(1, deletedComment.getPost().getCommentCount());

            ProcessStatus processStatus = commentService.deleteComment(commentId);
            Optional<Comment> comment = commentRepository.findById(commentId);
            assertEquals(Optional.empty(), comment);
            assertEquals(ProcessStatus.SUCCESS, processStatus);
            assertEquals(0, deletedComment.getPost().getCommentCount());
        }

        @DisplayName("타인이 타인 댓글 삭제")
        @Test
        void delete_comment2() {
            String displayId = "other";
            String nickname = "other";
            String email = "other@naver.com";
            String password = "other";
            SignupReqDto signupReqDto = SignupReqDto.builder()
                                                    .displayId(displayId)
                                                    .nickname(nickname)
                                                    .email(email)
                                                    .password(password)
                                                    .build();

            // 가입
            authService.signup(signupReqDto);
            Long otherId = memberRepository.findByEmail(email).get().getId();

            Authentication authentication = new UsernamePasswordAuthenticationToken(otherId, "", Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            assertEquals(ProcessStatus.FAIL, commentService.deleteComment(commentId));
            Optional<Comment> comment = commentRepository.findById(commentId);
            assertTrue(comment.isPresent());
        }

        @Test
        @DisplayName("댓글 불러오기")
        public void getComment_test() {
            CommentResDto dto = commentService.getCommentsFromPost(postId, null);
            assertEquals("test", dto.getCommentResDtos().get(0).getContent());
            assertEquals(1, dto.getCommentResDtos().size());
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

    @Nested
    @DisplayName("null 체크")
    class nullComment_test {
        @DisplayName("commentId = null로 들어오는 comment 삭제")
        @Test
        public void delete_comment1() {
            Exception exception = assertThrows(Exception.class, () -> commentService.deleteComment(commentId));
            assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
            assertEquals("The given id must not be null!", exception.getCause().getMessage());
        }

        @DisplayName("commentId = null로 들어오는 comment 좋아요")
        @Test
        public void like() {
            Exception exception = assertThrows(Exception.class, () -> commentService.likeComment(commentId));
            assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
            assertEquals("The given id must not be null!", exception.getCause().getMessage());
        }

        @DisplayName("commentId = null로 들어오는 comment 좋아요 취소")
        @Test
        public void unlike() {
            Exception exception = assertThrows(Exception.class, () -> commentService.unlikeComment(commentId));
            assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
            assertEquals("The given id must not be null!", exception.getCause().getMessage());
        }
    }

    @DisplayName("대댓글 조회하기")
    @Test
    void nestedComment_read() {
        CommentReqDto commentReqDto = new CommentReqDto("댓글");
        Post post = postRepository.findById(postId).get();
        CommentDto comment = commentService.createComment(postId, commentReqDto);
        commentId = post.getComments().get(0).getId();
        assertNotNull(comment);

        CommentReqDto commentReqDto2 = new CommentReqDto("대댓글");
        commentService.createNestedComment(commentId, commentReqDto2);

        CommentReqDto commentReqDto3 = new CommentReqDto("대댓글2");
        commentService.createNestedComment(commentId, commentReqDto3);

        final Comment baseComment = commentRepository.findById(commentId).get();

        assertThat(baseComment.getBaseComment()).isNull(); // 최상위 댓글
        assertThat(baseComment.getContent()).isEqualTo("댓글");
        assertThat(baseComment.getNestedComments().size()).isEqualTo(2);
        assertThat(baseComment.getNestedComments()).extracting("content").containsExactly("대댓글", "대댓글2");
    }

    @DisplayName("존재하지 않는 대댓글 조회 시도")
    @Test
    void is_not_nestedComment() {
        CommentReqDto commentReqDto = new CommentReqDto("댓글");
        Post post = postRepository.findById(postId).get();
        CommentDto commentDto = commentService.createComment(postId, commentReqDto);
        assertNotNull(commentDto);

        final Comment comment = post.getComments().get(0);

        assertThat(comment.getBaseComment()).isNull(); // 최상위 댓글
        assertThat(comment.getContent()).isEqualTo("댓글");
        assertThat(comment.getNestedComments().size()).isEqualTo(0);
    }
}
