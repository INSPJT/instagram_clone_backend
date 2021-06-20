package our.yurivongella.instagramclone.controller;

import io.swagger.annotations.ApiOperation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import our.yurivongella.instagramclone.controller.dto.PostResDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import our.yurivongella.instagramclone.service.PostService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FeedController {
    private final PostService postService;

    @ApiOperation("lastId보다 작은 5개의 인스타그램 피드 조회")
    @GetMapping("/feeds")
    public ResponseEntity<PostResDto> getFeeds(@RequestParam(name = "lastId", required = false) Long lastId) {
        return ResponseEntity.ok(postService.getFeeds(lastId));
    }
}
