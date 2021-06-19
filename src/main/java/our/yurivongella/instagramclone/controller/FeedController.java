package our.yurivongella.instagramclone.controller;

import io.swagger.annotations.ApiOperation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import our.yurivongella.instagramclone.controller.dto.post.PostReadResDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import our.yurivongella.instagramclone.service.PostService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FeedController {
    private final PostService postService;

    @ApiOperation("lastPostId 보다 작은 5 개의 인스타그램 피드 조회")
    @GetMapping("/feeds")
    public ResponseEntity<Slice<PostReadResDto>> getFeeds(Pageable pageable) {
        return ResponseEntity.ok(postService.getFeeds(pageable));
    }
}
