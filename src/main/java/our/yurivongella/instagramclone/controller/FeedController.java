package our.yurivongella.instagramclone.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import our.yurivongella.instagramclone.service.PostService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FeedController {
    private final PostService postService;

    @GetMapping("/feed/scroll")
    public ResponseEntity<?> getFeed(
            @PageableDefault(size = 3, sort = "p.createdDate", direction = Direction.DESC)
                    Pageable pageable) {
        log.info("----------------------------------페이지 쿼리를 요청합니다.-----------------------------------");
        return ResponseEntity.ok(postService.getFeed(pageable));
    }
}
