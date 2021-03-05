package our.yurivongella.instagramclone.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import our.yurivongella.instagramclone.controller.dto.post.PostCreateRequestDto;
import our.yurivongella.instagramclone.controller.dto.post.PostReadResponseDto;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.service.PostService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostApi {

    private final PostService postService;

    @PostMapping("")
    public ResponseEntity<String> create(@RequestBody PostCreateRequestDto postRequestDto) {
        /* 계정 작업 전 Test를 위한 임시 계정 */
        Member member = Member.builder()
                .name("슈르 킴")
                .email("tor01237@gmail.com")
                .nickName("Syureu")
                .password("1q2w3e4r!!")
                .build();
        Long id = postService.create(postRequestDto, member); // mock users
        return new ResponseEntity<>(id + "인 포스트가 만들어졌습니다.", HttpStatus.OK);
    }

    @PostMapping("/test/{userId}")
    public ResponseEntity<String> create(@RequestBody PostCreateRequestDto postRequestDto, @PathVariable Long userId) {
        Long id = postService.testCreate(postRequestDto, userId); // mock users
        return new ResponseEntity<>(id + "인 포스트가 만들어졌습니다.", HttpStatus.OK);
    }

    /*
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> read(@PathVariable Long postId) {
        return new ResponseEntity<>(postService.read(postId), HttpStatus.OK);
    }
     */

    /* 아래 Path의 userId는 추후 Token을 통해 인식 할 예 */
    @GetMapping("/{postId}/{userId}")
    public ResponseEntity<PostReadResponseDto> read(@PathVariable Long postId, @PathVariable Long userId) {
        PostReadResponseDto postResponseDto = postService.read(postId, userId);
        return new ResponseEntity<>(postResponseDto, HttpStatus.OK);
    }


    @DeleteMapping("/{postId}/{userId}")
    public ResponseEntity<String> delete(@PathVariable Long postId, @PathVariable Long usreId) {
        Long id = postService.delete(postId, usreId);
        return new ResponseEntity<>(id + "인 포스트가 삭제되었습니다.", HttpStatus.OK);
    }

}
