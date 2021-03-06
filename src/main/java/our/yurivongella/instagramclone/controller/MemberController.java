package our.yurivongella.instagramclone.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import our.yurivongella.instagramclone.controller.dto.FollowRequestDto;
import our.yurivongella.instagramclone.controller.dto.MemberResponseDto;
import our.yurivongella.instagramclone.service.MemberService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PutMapping("/follow")
    public ResponseEntity<String> follow(@RequestBody FollowRequestDto followRequestDto) {
        boolean success = memberService.follow(followRequestDto);
        return ResponseEntity.ok("팔로우 결과: " + success);
    }

    @PutMapping("/unfollow")
    public ResponseEntity<String> unfollow(@RequestBody FollowRequestDto followRequestDto) {
        boolean success = memberService.unFollow(followRequestDto);
        return ResponseEntity.ok("언팔로우 결과: " + success);
    }

    @GetMapping("/followers")
    public ResponseEntity<List<MemberResponseDto>> getFollowers() {
        return ResponseEntity.ok(memberService.getFollowers());
    }

    @GetMapping("/followings")
    public ResponseEntity<List<MemberResponseDto>> getFollowing() {
        return ResponseEntity.ok(memberService.getFollowings());
    }
}
