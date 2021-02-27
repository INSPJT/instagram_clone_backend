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
import our.yurivongella.instagramclone.controller.dto.MemberRequestDto;
import our.yurivongella.instagramclone.controller.dto.MemberResponseDto;
import our.yurivongella.instagramclone.domain.follow.Follow;
import our.yurivongella.instagramclone.service.MemberService;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signUp")
    public ResponseEntity signUp(@RequestBody MemberRequestDto memberRequestDto){
        Long id = memberService.signUp(memberRequestDto);
        return ResponseEntity.ok(id+"로 저장되었습니다.");
    }

    @PutMapping("/follow")
    public ResponseEntity follow(@RequestBody FollowRequestDto followRequestDto){
        boolean follow = memberService.follow(followRequestDto.getFromMember(), followRequestDto.getToMember());
        return ResponseEntity.ok("결과:"+follow);
    }

    @PutMapping("/unfollow")
    public ResponseEntity unfollow(@RequestBody FollowRequestDto followRequestDto){
        boolean followResult = memberService.unFollow(followRequestDto.getFromMember(), followRequestDto.getToMember());
        return ResponseEntity.ok("결과:"+followResult);
    }

    @GetMapping("/getFollowers/{id}")
    public ResponseEntity getFollowers(@PathVariable Long id){
        final List<Follow> followers = memberService.getFollowers(id);
        // from - to(여기)
        FollowerResponseDto response = new FollowerResponseDto();
        for(Follow follow : followers) response.getFollowers().add(new MemberResponseDto(follow.getFromMember().getName()));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getFollowings/{id}")
    public ResponseEntity getFollowing(@PathVariable Long id){
        final List<Follow> following = memberService.getFollowing(id);
        // from(여기) - to
        final FollowerResponseDto response = new FollowerResponseDto();
        for(Follow follow : following) response.getFollowers().add(new MemberResponseDto(follow.getToMember().getName()));
        return ResponseEntity.ok(response);
    }
}
