package our.yurivongella.instagramclone.controller;

import java.util.List;

import io.swagger.annotations.ApiOperation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import our.yurivongella.instagramclone.controller.dto.member.MemberResDto;
import our.yurivongella.instagramclone.controller.dto.ProcessStatus;
import our.yurivongella.instagramclone.service.FollowService;
import our.yurivongella.instagramclone.service.MemberService;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final FollowService followService;
    private final MemberService memberService;

    @ApiOperation("내 정보 조회")
    @GetMapping("/member")
    public ResponseEntity<MemberResDto> getMyProfile() {
        return ResponseEntity.ok(memberService.getMyProfile());
    }

    @ApiOperation("특정 사용자의 정보 조회")
    @GetMapping("/members/{displayId}")
    public ResponseEntity<MemberResDto> getProfile(@PathVariable String displayId) {
        return ResponseEntity.ok(memberService.getProfile(displayId));
    }

    @ApiOperation("상대방 팔로우")
    @PutMapping("/member/follow/{displayId}")
    public ResponseEntity<ProcessStatus> follow(@PathVariable String displayId) {
        return ResponseEntity.ok(followService.follow(displayId));
    }

    @ApiOperation("상대방 언팔로우")
    @DeleteMapping("/member/follow/{displayId}")
    public ResponseEntity<ProcessStatus> unfollow(@PathVariable String displayId) {
        return ResponseEntity.ok(followService.unFollow(displayId));
    }

    @ApiOperation("나를 팔로우 하는 Followers 조회")
    @GetMapping("/member/followers")
    public ResponseEntity<List<MemberResDto>> getFollowers() {
        return ResponseEntity.ok(followService.getFollowers());
    }

    @ApiOperation("내가 팔로우 하는 Followings 조회")
    @GetMapping("/member/followings")
    public ResponseEntity<List<MemberResDto>> getFollowing() {
        return ResponseEntity.ok(followService.getFollowings());
    }

    @ApiOperation("회원 상태 변경")
    @PutMapping("/member/activate")
    public ResponseEntity<ProcessStatus> activate(@RequestParam("state") boolean state){
        ProcessStatus result = state ? memberService.activate() : memberService.deactivate();
        return ResponseEntity.ok(result);
    }
}
