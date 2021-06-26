package our.yurivongella.instagramclone.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import our.yurivongella.instagramclone.controller.dto.ProcessStatus;
import our.yurivongella.instagramclone.controller.dto.post.PostDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import our.yurivongella.instagramclone.controller.dto.post.PostReqDto;
import our.yurivongella.instagramclone.controller.dto.post.PostResDto;
import our.yurivongella.instagramclone.service.PostService;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @ApiOperation("새로운 게시글 생성")
    @PostMapping("/posts")
    public ResponseEntity<PostDto> create(@RequestBody PostReqDto postReqDto) {
        return ResponseEntity.ok(postService.createPost(postReqDto));
    }

    @ApiOperation("특정 게시글 하나 가져오기")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @ApiOperation("특정 게시글 하나 삭제")
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        ProcessStatus processStatus = postService.deletePost(postId);
        return ResponseEntity.ok(processStatus.getMessage());
    }

    @ApiOperation("포스트 좋아요")
    @PutMapping("/posts/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable Long postId) {
        ProcessStatus processStatus = postService.likePost(postId);
        return ResponseEntity.ok(processStatus.getMessage());
    }

    @ApiOperation("포스트 좋아요 취소")
    @DeleteMapping("/posts/{postId}/like")
    public ResponseEntity<String> unLikePost(@PathVariable Long postId) {
        ProcessStatus processStatus = postService.unlikePost(postId);
        return ResponseEntity.ok(processStatus.getMessage());
    }

    @ApiOperation("lastId 보다 작은 5개의 인스타그램 피드 조회")
    @GetMapping("/feeds")
    public ResponseEntity<PostResDto> getFeeds(@RequestParam(name = "lastId", required = false) Long lastId) {
        return ResponseEntity.ok(postService.getFeeds(lastId));
    }

    @ApiOperation("내 게시글 리스트 조회 (lastPostId 기준으로 최대 12개씩)")
    @GetMapping("/member/posts")
    public ResponseEntity<PostResDto> getMyPosts(@RequestParam(name = "lastId", required = false) Long lastId) {
        return ResponseEntity.ok(postService.getMyPosts(lastId));
    }

    @ApiOperation("특정 사용자의 게시글 리스트 조회 (lastPostId 기준으로 최대 12개씩)")
    @GetMapping("/members/{displayId}/posts")
    public ResponseEntity<PostResDto> getPosts(@PathVariable String displayId, @RequestParam(name = "lastId", required = false) Long lastId) {
        return ResponseEntity.ok(postService.getPosts(displayId, lastId));
    }
}
