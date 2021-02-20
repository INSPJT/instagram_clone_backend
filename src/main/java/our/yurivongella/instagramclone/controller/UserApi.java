package our.yurivongella.instagramclone.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import our.yurivongella.instagramclone.controller.dto.FollowRequestDto;
import our.yurivongella.instagramclone.controller.dto.FollowerResponseDto;
import our.yurivongella.instagramclone.controller.dto.UsersRequestDto;
import our.yurivongella.instagramclone.controller.dto.UsersResponseDto;
import our.yurivongella.instagramclone.domain.Follow.Follow;
import our.yurivongella.instagramclone.service.UserService;

@RequiredArgsConstructor
@RestController
public class UserApi {

    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity signUp(@RequestBody UsersRequestDto usersRequestDto){
        Long id = userService.signUp(usersRequestDto);
        return ResponseEntity.ok(id+"로 저장되었습니다.");
    }

    @PutMapping("/follow")
    public ResponseEntity follow(@RequestBody FollowRequestDto followRequestDto){
        boolean follow = userService.follow(followRequestDto.getFromUser(), followRequestDto.getToUser());
        return ResponseEntity.ok("결과:"+follow);
    }

    @PutMapping("/unfollow")
    public ResponseEntity unfollow(@RequestBody FollowRequestDto followRequestDto){
        boolean followResult = userService.unFollow(followRequestDto.getFromUser(), followRequestDto.getToUser());
        return ResponseEntity.ok("결과:"+followResult);
    }

    @GetMapping("/getFollowers/{id}")
    public ResponseEntity getFollowers(@PathVariable Long id){
        final List<Follow> followers = userService.getFollowers(id);
        // from - to(여기)
        FollowerResponseDto response = new FollowerResponseDto();
        for(Follow follow : followers) response.getFollowers().add(new UsersResponseDto(follow.getFromUser().getName()));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getFollowings/{id}")
    public ResponseEntity getFollowing(@PathVariable Long id){
        final List<Follow> following = userService.getFollowing(id);
        // from(여기) - to
        final FollowerResponseDto response = new FollowerResponseDto();
        for(Follow follow : following) response.getFollowers().add(new UsersResponseDto(follow.getToUser().getName()));
        return ResponseEntity.ok(response);
    }
}
