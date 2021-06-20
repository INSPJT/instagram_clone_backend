package our.yurivongella.instagramclone.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import our.yurivongella.instagramclone.service.StoryService;

@RestController
@RequiredArgsConstructor
public class StoryController {
    private final StoryService storyService;

    @ApiOperation("나의 Story List 보기")
    @GetMapping("story/my-story")
    public ResponseEntity<?> getMyStories() {
        return ResponseEntity.ok(storyService.getMyStories());
    }

}
