package our.yurivongella.instagramclone.controller;

import java.util.List;

import io.swagger.annotations.ApiOperation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import our.yurivongella.instagramclone.controller.dto.member.MemberResponseDto;
import our.yurivongella.instagramclone.controller.dto.ProcessStatus;
import our.yurivongella.instagramclone.service.FollowService;
import our.yurivongella.instagramclone.service.MemberService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {
    private final FollowService followService;
    private final MemberService memberService;

    @ApiOperation("상대방 팔로우")
    @PutMapping("/follow/{displayId}")
    public ResponseEntity<ProcessStatus> follow(@PathVariable String displayId) {
        return ResponseEntity.ok(followService.follow(displayId));
    }

    @ApiOperation("상대방 언팔로우")
    @DeleteMapping("/follow/{displayId}")
    public ResponseEntity<ProcessStatus> unfollow(@PathVariable String displayId) {
        return ResponseEntity.ok(followService.unFollow(displayId));
    }

    @ApiOperation("나를 팔로우 하는 Followers 조회")
    @GetMapping("/followers")
    public ResponseEntity<List<MemberResponseDto>> getFollowers() {
        return ResponseEntity.ok(followService.getFollowers());
    }

    @ApiOperation("내가 팔로우 하는 Followings 조회")
    @GetMapping("/followings")
    public ResponseEntity<List<MemberResponseDto>> getFollowing() {
        return ResponseEntity.ok(followService.getFollowings());
    }

    @ApiOperation("회원 상태 변경")
    @PutMapping("/activate")
    public ResponseEntity<ProcessStatus> activate(@RequestParam("state") boolean state){
        ProcessStatus result = state ? memberService.activate() : memberService.deactivate();
        return ResponseEntity.ok(result);
    }
}
