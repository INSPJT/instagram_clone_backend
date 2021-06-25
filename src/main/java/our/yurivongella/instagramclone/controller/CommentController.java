package our.yurivongella.instagramclone.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import our.yurivongella.instagramclone.controller.dto.ProcessStatus;
import our.yurivongella.instagramclone.controller.dto.comment.CommentDto;
import our.yurivongella.instagramclone.controller.dto.comment.CommentReqDto;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import our.yurivongella.instagramclone.controller.dto.comment.CommentResDto;
import our.yurivongella.instagramclone.service.CommentService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @ApiOperation("게시글의 댓글 리스트 조회")
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResDto> getComments(@PathVariable("postId") Long postId, @RequestParam(value = "lastId", required = false) Long lastId) {
        return ResponseEntity.ok(commentService.getCommentsFromPost(postId, lastId));
    }

    @ApiOperation("게시글에 새로운 댓글 생성")
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable("postId") Long postId, @RequestBody @Valid CommentReqDto commentReqDto) {
        return ResponseEntity.ok(commentService.createComment(postId, commentReqDto));
    }

    @ApiOperation("해당 댓글 삭제")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("commentId") Long commentId) {
        ProcessStatus processStatus = commentService.deleteComment(commentId);
        return ResponseEntity.ok(processStatus.getMessage());
    }

    @ApiOperation("댓글에 좋아요")
    @PutMapping("/comments/{commentId}/like")
    public ResponseEntity<String> likeComment(@PathVariable("commentId") Long commentId) throws Exception {
        ProcessStatus processStatus = commentService.likeComment(commentId);
        return ResponseEntity.ok(processStatus.getMessage());
    }

    @ApiOperation("댓글 좋아요 취소")
    @DeleteMapping("/comments/{commentId}/like")
    public ResponseEntity<String> unlikeComment(@PathVariable("commentId") Long commentId) throws Exception {
        ProcessStatus processStatus = commentService.unlikeComment(commentId);
        return ResponseEntity.ok(processStatus.getMessage());
    }

    @ApiOperation("대댓글 정보 가져오기")
    @GetMapping("/comments/{commentId}/nested-comments")
    public ResponseEntity<CommentResDto> getNestedComments(@PathVariable("commentId") Long commentId, @RequestParam(value = "lastId", required = false) Long lastId) {
        return ResponseEntity.ok(commentService.findNestedComments(commentId, lastId));
    }

    @ApiOperation("대댓글 달기")
    @PostMapping("/comments/{commentId}/nested-comments")
    public ResponseEntity<CommentDto> createNestedComment(@PathVariable("commentId") Long commentId, @RequestBody @Valid CommentReqDto commentReqDto) {
        return ResponseEntity.ok(commentService.createNestedComment(commentId, commentReqDto));
    }
}
