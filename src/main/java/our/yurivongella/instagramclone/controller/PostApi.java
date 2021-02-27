package our.yurivongella.instagramclone.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import our.yurivongella.instagramclone.controller.dto.post.PostRequestDto;
import our.yurivongella.instagramclone.controller.dto.post.PostResponseDto;
import our.yurivongella.instagramclone.domain.Users.Users;
import our.yurivongella.instagramclone.service.PostService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostApi {

    private final PostService postService;

    @PostMapping("")
    public ResponseEntity<String> create(@RequestBody PostRequestDto postRequestDto) {
        Users user = Users.builder()
                .name("슈르 킴")
                .email("tor01237@gmail.com")
                .nickName("Syureu")
                .password("1q2w3e4r!!")
                .build();
        Long id = postService.create(postRequestDto, user); // mock users
        return new ResponseEntity<>(id + "인 포스트가 만들어졌습니다.", HttpStatus.OK);
    }

    @PostMapping("/test/{userId}")
    public ResponseEntity<String> create(@RequestBody PostRequestDto postRequestDto, @PathVariable Long userId) {
        Long id = postService.testCreate(postRequestDto, userId); // mock users
        return new ResponseEntity<>(id + "인 포스트가 만들어졌습니다.", HttpStatus.OK);
    }

    /*
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> read(@PathVariable Long postId) {
        return new ResponseEntity<>(postService.read(postId), HttpStatus.OK);
    }
     */

    @GetMapping("/{postId}/{userId}")
    public ResponseEntity<PostResponseDto> read(@PathVariable Long postId, @PathVariable Long userId) {
        PostResponseDto postResponseDto = postService.read(postId, userId);
        return new ResponseEntity<>(postResponseDto, HttpStatus.OK);
    }
}
