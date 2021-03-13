package our.yurivongella.instagramclone.controller;

import java.util.List;

import io.swagger.annotations.ApiOperation;
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

    @ApiOperation("상대방 팔로우")
    @PutMapping("/follow/{memberId}")
    public ResponseEntity<String> follow(@PathVariable Long memberId) {
        boolean success = memberService.follow(memberId);
        return ResponseEntity.ok("팔로우 결과: " + success);
    }

    @ApiOperation("상대방 언팔로우")
    @DeleteMapping("/follow/{memberId}")
    public ResponseEntity<String> unfollow(@PathVariable Long memberId) {
        boolean success = memberService.unFollow(memberId);
        return ResponseEntity.ok("언팔로우 결과: " + success);
    }

    @ApiOperation("나를 팔로우 하는 Followers 조회")
    @GetMapping("/followers")
    public ResponseEntity<List<MemberResponseDto>> getFollowers() {
        return ResponseEntity.ok(memberService.getFollowers());
    }

    @ApiOperation("내가 팔로우 하는 Followings 조회")
    @GetMapping("/followings")
    public ResponseEntity<List<MemberResponseDto>> getFollowing() {
        return ResponseEntity.ok(memberService.getFollowings());
    }
}
