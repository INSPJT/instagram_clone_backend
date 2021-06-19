package our.yurivongella.instagramclone.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import our.yurivongella.instagramclone.controller.dto.profile.ProfileDto;
import our.yurivongella.instagramclone.controller.dto.profile.ProfilePostDto;
import our.yurivongella.instagramclone.controller.dto.profile.SimpleProfileDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import our.yurivongella.instagramclone.service.ProfileService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @ApiOperation("내 Display Id, Profile Image Url 만 조회")
    @GetMapping("/member")
    public ResponseEntity<SimpleProfileDto> getMySimpleProfile() {
        return ResponseEntity.ok(profileService.getMySimpleProfile());
    }

    @ApiOperation("내 프로필 조회")
    @GetMapping("/member/profiles")
    public ResponseEntity<ProfileDto> getMyProfile() {
        return ResponseEntity.ok(profileService.getMyProfile());
    }

    @ApiOperation("특정 사용자의 프로필 조회")
    @GetMapping("/members/{displayId}/profiles")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable String displayId) {
        return ResponseEntity.ok(profileService.getProfile(displayId));
    }

    @ApiOperation("내 게시글 리스트 조회 (lastPostId 기준으로 최대 12개씩)")
    @GetMapping("/member/posts")
    public ResponseEntity<List<ProfilePostDto>> getMyPosts(@RequestParam(required = false) Long lastPostId) {
        return ResponseEntity.ok(profileService.getMyPosts(lastPostId));
    }

    @ApiOperation("특정 사용자의 게시글 리스트 조회 (lastPostId 기준으로 최대 12개씩)")
    @GetMapping("/members/{displayId}/posts")
    public ResponseEntity<List<ProfilePostDto>> getPosts(@PathVariable String displayId, @RequestParam(required = false) Long lastPostId) {
        return ResponseEntity.ok(profileService.getPosts(displayId, lastPostId));
    }
}
