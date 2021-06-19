package our.yurivongella.instagramclone.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import our.yurivongella.instagramclone.controller.dto.comment.CommentCreateDto;
import our.yurivongella.instagramclone.controller.dto.ProcessStatus;
import our.yurivongella.instagramclone.controller.dto.comment.CommentResponseDto;
import our.yurivongella.instagramclone.service.CommentService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @ApiOperation("게시글의 댓글 리스트 조회")
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<?> getComments(@PathVariable("postId") Long postId) {
        List<CommentResponseDto> comments = commentService.getComments(postId);
        return ResponseEntity.ok(comments);
    }

    @ApiOperation("게시글에 content 로 새로운 댓글 생성")
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<?> createComment(@PathVariable("postId") Long postId, @RequestBody @Valid CommentCreateDto commentCreateDto) {
        return ResponseEntity.ok(commentService.createComment(postId, commentCreateDto));
    }

    @ApiOperation("대댓글 달기")
    @PostMapping("/comments/{commentId}/comments")
    public ResponseEntity<?> createNestedComment(@PathVariable("commentId") Long commentId, @RequestBody @Valid CommentCreateDto commentCreateDto) {
        return ResponseEntity.ok(commentService.createNestedComment(commentId, commentCreateDto));
    }

    @ApiOperation("해당 댓글 삭제")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId) throws Exception {
        ProcessStatus processStatus = commentService.deleteComment(commentId);
        return ResponseEntity.ok(processStatus.getMessage());
    }

    @ApiOperation("댓글에 좋아요")
    @PutMapping("/comments/{commentId}/like")
    public ResponseEntity<?> likeComment(@PathVariable("commentId") Long commentId) throws Exception {
        ProcessStatus processStatus = commentService.likeComment(commentId);
        return ResponseEntity.ok(processStatus.getMessage());
    }

    @ApiOperation("댓글 좋아요 취소")
    @DeleteMapping("/comments/{commentId}/like")
    public ResponseEntity<?> unlikeComment(@PathVariable("commentId") Long commentId) throws Exception {
        ProcessStatus processStatus = commentService.unlikeComment(commentId);
        return ResponseEntity.ok(processStatus.getMessage());
    }

}
