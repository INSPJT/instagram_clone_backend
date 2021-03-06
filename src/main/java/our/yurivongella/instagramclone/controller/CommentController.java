package our.yurivongella.instagramclone.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import our.yurivongella.instagramclone.controller.dto.CommentCreateDto;
import our.yurivongella.instagramclone.controller.dto.comment.ProcessStatus;
import our.yurivongella.instagramclone.controller.dto.post.CommentResponseDto;
import our.yurivongella.instagramclone.service.CommentService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @ApiOperation("postId를 넘겨주면 해당 포스트의 댓글들을 조회합니다.")
    @GetMapping("/{postId}")
    public ResponseEntity<?> getComments(@PathVariable("postId") Long postId) {
        List<CommentResponseDto> comments = commentService.getComments(postId);
        return ResponseEntity.ok(comments);
    }

    @ApiOperation("postId에 해당하는 포스트에 content를 넘겨주면 댓글이 생성됩니다.")
    @PostMapping("/{postId}")
    public ResponseEntity<?> createComment(@PathVariable("postId") Long postId, @RequestBody CommentCreateDto commentCreateDto) {
        return ResponseEntity.ok(commentService.createComment(postId, commentCreateDto));
    }

    @ApiOperation("commentId에 해당하는 댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId) throws Exception {
        ProcessStatus processStatus = commentService.deleteComment(commentId);
        return ResponseEntity.ok(processStatus.getMessage());
    }

    @ApiOperation("commentId에 해당하는 댓글을 좋아요 표시합니다.")
    @PutMapping("/like/{commentId}")
    public ResponseEntity<?> likeComment(@PathVariable("commentId") Long commentId) throws Exception {
        ProcessStatus processStatus = commentService.likeComment(commentId);
        return ResponseEntity.ok(processStatus.getMessage());
    }

    @ApiOperation("commentId에 해당하는 댓글 좋아요를 취소합니다.")
    @DeleteMapping("/like/{commentId}")
    public ResponseEntity<?> unlikeComment(@PathVariable("commentId") Long commentId) throws Exception {
        ProcessStatus processStatus = commentService.unlikeComment(commentId);
        return ResponseEntity.ok(processStatus.getMessage());
    }

}
