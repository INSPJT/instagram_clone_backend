package our.yurivongella.instagramclone.service;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import our.yurivongella.instagramclone.controller.dto.comment.CommentReqDto;
import our.yurivongella.instagramclone.controller.dto.comment.CommentResDto;
import our.yurivongella.instagramclone.controller.dto.ProcessStatus;
import our.yurivongella.instagramclone.controller.dto.comment.CommentDto;
import our.yurivongella.instagramclone.controller.dto.post.PostCreateReqDto;
import our.yurivongella.instagramclone.entity.Comment;
import our.yurivongella.instagramclone.repository.comment.CommentRepository;
import our.yurivongella.instagramclone.exception.CustomException;
import our.yurivongella.instagramclone.exception.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("댓글 Service 테스트")
class CommentServiceTest extends TestBase {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostService postService;

    @BeforeEach
    void loginBeforeTest() {
        signupAndLogin("testComment", "testComment@test.net");
    }

    @DisplayName("댓글 없을 때 가져오면 비어있는 응답 제공")
    @Test
    void testGetCommentsFromPost() {
        // given
        Long postId = postService.create(new PostCreateReqDto(Collections.emptyList(), "test post"));

        // then
        CommentResDto dto =  commentService.getCommentsFromPost(postId, null);

        assertThat(dto.getHasNext()).isFalse();
        assertThat(dto.getComments()).isEmpty();
    }

    @DisplayName("댓글 추가 테스트")
    @Nested
    class createCommentTest {

        @DisplayName("댓글 2 개 추가 성공")
        @Test
        void success() {
            // given
            Long postId = postService.create(new PostCreateReqDto(Collections.emptyList(), "test post"));

            // when
            commentService.createComment(postId, new CommentReqDto("test comment"));
            commentService.createComment(postId, new CommentReqDto("test comment2"));

            // then
            CommentResDto commentResDto = commentService.getCommentsFromPost(postId, null);
            List<CommentDto> commentDtos = commentResDto.getComments();

            assertThat(commentDtos.size()).isEqualTo(2);

            CommentDto commentDto1 = commentDtos.get(0);
            assertThat(commentDto1.getContent()).isEqualTo("test comment2");
            assertThat(commentDto1.getLikeCount()).isEqualTo(0);
            assertThat(commentDto1.getNestedCommentCount()).isEqualTo(0);
            assertThat(commentDto1.getAuthor().getDisplayId()).isEqualTo("testComment");

            CommentDto commentDto2 = commentDtos.get(1);
            assertThat(commentDto2.getContent()).isEqualTo("test comment");
            assertThat(commentDto2.getLikeCount()).isEqualTo(0);
            assertThat(commentDto2.getNestedCommentCount()).isEqualTo(0);
            assertThat(commentDto2.getAuthor().getDisplayId()).isEqualTo("testComment");
        }

        @DisplayName("게시글이 없는 경우 댓글 달기 실패")
        @Test
        void failWhenNonExistentPost() {
            CommentReqDto commentReqDto = new CommentReqDto("test comment");

            CustomException ex = assertThrows(CustomException.class, () -> commentService.createComment(-1L, commentReqDto));

            Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, ex.getErrorCode());
        }
    }

    @DisplayName("댓글 삭제 테스트")
    @Nested
    class deleteCommentTest {

        @DisplayName("댓글 삭제 성공")
        @Test
        void successDelete() {
            // given
            Long postId = postService.create(new PostCreateReqDto(Collections.emptyList(), "test post"));
            CommentDto commentDto = commentService.createComment(postId, new CommentReqDto("test comment"));
            Comment deletedComment = commentRepository.findById(commentDto.getId()).get();

            // when
            ProcessStatus processStatus = commentService.deleteComment(commentDto.getId());

            // then
            assertThat(processStatus).isEqualTo(ProcessStatus.SUCCESS);
            assertThat(commentRepository.findById(commentDto.getId())).isEmpty();
            assertThat(deletedComment.getPost().getCommentCount()).isEqualTo(0);
        }

        @DisplayName("없는 댓글 지우려 하면 실패")
        @Test
        void failWhenDeleteNonExistentComment() {
            CustomException ex = assertThrows(CustomException.class, () -> commentService.deleteComment(-1L));
            Assertions.assertEquals(ErrorCode.COMMENT_NOT_FOUND, ex.getErrorCode());
        }

        @DisplayName("다른 사람의 댓글 지우려 하면 실패")
        @Test
        void failWhenDeleteNotMineComment() {
            // testComment 계정으로 댓글 작성 후 other 계정으로 댓글 삭제 시도
            Long postId = postService.create(new PostCreateReqDto(Collections.emptyList(), "test post"));
            CommentDto commentDto = commentService.createComment(postId, new CommentReqDto("test comment"));
            Comment deletedComment = commentRepository.findById(commentDto.getId()).get();

            // when
            signupAndLogin("other", "other@test.net");
            ProcessStatus processStatus = commentService.deleteComment(commentDto.getId());

            // then
            assertThat(processStatus).isEqualTo(ProcessStatus.FAIL);
            assertThat(commentRepository.findById(commentDto.getId())).isPresent();
            assertThat(deletedComment.getPost().getCommentCount()).isEqualTo(1);
        }
    }

    @DisplayName("좋아요 테스트")
    @Nested
    class likeCommentTest {

        @DisplayName("좋아요 성공")
        @Test
        void successLike() {
            // given
            Long postId = postService.create(new PostCreateReqDto(Collections.emptyList(), "test post"));
            CommentDto commentDto = commentService.createComment(postId, new CommentReqDto("test comment"));

            // when
            ProcessStatus processStatus = commentService.likeComment(commentDto.getId());

            // then
            assertThat(processStatus).isEqualTo(ProcessStatus.SUCCESS);
            Comment comment = commentRepository.findById(commentDto.getId()).get();
            assertThat(comment.getLikeCount()).isEqualTo(1);
        }

        @DisplayName("없는 댓글에 좋아요 하려고 하면 실패")
        @Test
        void failWhenNonExistentComment() {
            CustomException ex = assertThrows(CustomException.class, () -> commentService.likeComment(-1L));
            assertEquals(ErrorCode.COMMENT_NOT_FOUND, ex.getErrorCode());
        }

        @DisplayName("이미 좋아요 되어 있으면 실패")
        @Test
        void failWhenAlreadyLiked() {
            // given
            Long postId = postService.create(new PostCreateReqDto(Collections.emptyList(), "test post"));
            CommentDto commentDto = commentService.createComment(postId, new CommentReqDto("test comment"));

            // when
            commentService.likeComment(commentDto.getId());

            // then
            CustomException ex = assertThrows(CustomException.class, () -> commentService.likeComment(commentDto.getId()));
            assertEquals(ErrorCode.ALREADY_LIKE, ex.getErrorCode());
        }
    }

    @DisplayName("좋아요 취소 테스트")
    @Nested
    class unlikeCommentTest {

        @DisplayName("좋아요 취소 성공")
        @Test
        void successUnlike() {
            // given
            Long postId = postService.create(new PostCreateReqDto(Collections.emptyList(), "test post"));
            CommentDto commentDto = commentService.createComment(postId, new CommentReqDto("test comment"));

            // when
            commentService.likeComment(commentDto.getId());
            ProcessStatus processStatus = commentService.unlikeComment(commentDto.getId());

            // then
            assertThat(processStatus).isEqualTo(ProcessStatus.SUCCESS);
            Comment comment = commentRepository.findById(commentDto.getId()).get();
            assertThat(comment.getLikeCount()).isEqualTo(0);
        }

        @DisplayName("없는 댓글에 좋아요 취소 하려고 하면 실패")
        @Test
        void failWhenNonExistentComment() {
            CustomException ex = assertThrows(CustomException.class, () -> commentService.unlikeComment(-1L));
            assertEquals(ErrorCode.COMMENT_NOT_FOUND, ex.getErrorCode());
        }

        @DisplayName("좋아요 되어있지 않으면 실패")
        @Test
        void failWhenAlreadyUnlike() {
            // given
            Long postId = postService.create(new PostCreateReqDto(Collections.emptyList(), "test post"));
            CommentDto commentDto = commentService.createComment(postId, new CommentReqDto("test comment"));

            // when
            CustomException ex = assertThrows(CustomException.class, () -> commentService.unlikeComment(commentDto.getId()));

            // then
            assertEquals(ErrorCode.ALREADY_UNLIKE, ex.getErrorCode());
        }
    }

    @DisplayName("null 체크")
    @Nested
    class NullCommentIdTest {

        @BeforeEach
        void beforeEach() {
            postService.create(new PostCreateReqDto(Collections.emptyList(), "test post"));
        }

        @DisplayName("commentId = null 로 들어오는 comment 삭제 실패")
        @Test
        void deleteComment() {
            Exception exception = assertThrows(Exception.class, () -> commentService.deleteComment(null));
            assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
            assertEquals("The given id must not be null!", exception.getCause().getMessage());
        }

        @DisplayName("commentId = null 로 들어오는 comment 좋아요 실패")
        @Test
        void like() {
            Exception exception = assertThrows(Exception.class, () -> commentService.likeComment(null));
            assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
            assertEquals("The given id must not be null!", exception.getCause().getMessage());
        }

        @DisplayName("commentId = null 로 들어오는 comment 좋아요 취소 실패")
        @Test
        void unlike() {
            Exception exception = assertThrows(Exception.class, () -> commentService.unlikeComment(null));
            assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
            assertEquals("The given id must not be null!", exception.getCause().getMessage());
        }
    }

    @DisplayName("대댓글 테스트")
    @Nested
    class NestedCommentTest {

        @DisplayName("대댓글 생성 및 조회 성공")
        @Test
        void successCreateAndRead() {
            // given
            Long postId = postService.create(new PostCreateReqDto(Collections.emptyList(), "test post"));
            CommentDto commentDto = commentService.createComment(postId, new CommentReqDto("test comment"));

            // when
            commentService.createNestedComment(commentDto.getId(), new CommentReqDto("대댓글"));
            commentService.createNestedComment(commentDto.getId(), new CommentReqDto("대댓글2"));

            // then
            CommentResDto nestedComments = commentService.findNestedComments(commentDto.getId(), null);
            Comment baseComment = commentRepository.findById(commentDto.getId()).get();

            assertThat(nestedComments.getHasNext()).isFalse();
            assertThat(nestedComments.getComments()).isNotEmpty();

            assertThat(baseComment.getBaseComment()).isNull(); // 최상위 댓글
            assertThat(baseComment.getContent()).isEqualTo("test comment");
            assertThat(baseComment.getNestedComments().size()).isEqualTo(2);
            assertThat(baseComment.getNestedComments()).extracting("content").containsExactly("대댓글", "대댓글2");
        }

        @DisplayName("존재하지 않는 대댓글 조회 성공")
        @Test
        void failWhenNonExistentNestedComment() {
            // given
            Long postId = postService.create(new PostCreateReqDto(Collections.emptyList(), "test post"));
            CommentDto commentDto = commentService.createComment(postId, new CommentReqDto("test comment"));

            // when
            CommentResDto nestedComments = commentService.findNestedComments(commentDto.getId(), null);
            Comment baseComment = commentRepository.findById(commentDto.getId()).get();

            // then
            assertThat(nestedComments.getHasNext()).isFalse();
            assertThat(nestedComments.getComments()).isEmpty();

            assertThat(baseComment.getBaseComment()).isNull(); // 최상위 댓글
            assertThat(baseComment.getContent()).isEqualTo("test comment");
            assertThat(baseComment.getNestedComments().size()).isEqualTo(0);
        }
    }
}
