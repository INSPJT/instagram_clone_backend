package our.yurivongella.instagramclone.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import our.yurivongella.instagramclone.controller.dto.comment.ProcessStatus;
import our.yurivongella.instagramclone.controller.dto.post.PostCreateRequestDto;
import our.yurivongella.instagramclone.controller.dto.post.PostReadResponseDto;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.member.MemberRepository;
import our.yurivongella.instagramclone.service.PostService;
import our.yurivongella.instagramclone.util.SecurityUtil;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody PostCreateRequestDto postRequestDto) {
        Long id = postService.create(postRequestDto); // mock users
        return ResponseEntity.ok(id + "인 포스트가 만들어졌습니다.");
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostReadResponseDto> read(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.read(postId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> delete(@PathVariable Long postId) {
        ProcessStatus processStatus = postService.delete(postId);
        return ResponseEntity.ok(processStatus.getMessage());
    }

    @GetMapping("/members/{memberId}/posts")
    public ResponseEntity<?> readPostList(@PathVariable Long memberId) {
        return ResponseEntity.ok(postService.getPostList(memberId));
    }
}
