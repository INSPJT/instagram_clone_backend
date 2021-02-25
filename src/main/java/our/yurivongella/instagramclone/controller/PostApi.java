package our.yurivongella.instagramclone.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import our.yurivongella.instagramclone.controller.dto.PostRequestDto;
import our.yurivongella.instagramclone.service.PostService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostApi {

    private final PostService postService;

    @PostMapping("")
    public ResponseEntity<String> create(@RequestBody PostRequestDto postRequestDto) {
        Long id = postService.create(postRequestDto); // mock users
        return new ResponseEntity<>(id + "인 포스트가 만들어졌습니다.", HttpStatus.OK);
    }

}
