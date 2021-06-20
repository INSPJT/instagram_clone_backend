package our.yurivongella.instagramclone.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import our.yurivongella.instagramclone.controller.dto.comment.ProcessStatus;
import our.yurivongella.instagramclone.controller.dto.post.PostCreateRequestDto;
import our.yurivongella.instagramclone.controller.dto.post.PostReadResponseDto;
import our.yurivongella.instagramclone.service.PostService;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @ApiOperation("새로운 게시글 생성")
    @PostMapping("/posts")
    public ResponseEntity<String> create(@RequestBody PostCreateRequestDto postRequestDto) {
        Long id = postService.create(postRequestDto); // mock users
        return ResponseEntity.ok(id + "인 포스트가 만들어졌습니다.");
    }

    @ApiOperation("특정 게시글 하나 가져오기")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostReadResponseDto> read(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.read(postId));
    }

    @ApiOperation("특정 게시글 하나 삭제")
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> delete(@PathVariable Long postId) {
        ProcessStatus processStatus = postService.delete(postId);
        return ResponseEntity.ok(processStatus.getMessage());
    }

    @ApiOperation("포스트 좋아요")
    @PutMapping("/posts/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.likePost(postId));
    }

    @ApiOperation("포스트 좋아요 취소")
    @DeleteMapping("/posts/{postId}/like")
    public ResponseEntity<?> unLikePost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.unlikePost(postId));
    }
}
