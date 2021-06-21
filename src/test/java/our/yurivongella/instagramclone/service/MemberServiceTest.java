package our.yurivongella.instagramclone.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import our.yurivongella.instagramclone.controller.dto.member.MemberResDto;
import our.yurivongella.instagramclone.entity.Member;
import our.yurivongella.instagramclone.exception.CustomException;
import our.yurivongella.instagramclone.exception.ErrorCode;

@Transactional
class MemberServiceTest extends TestBase {

    @Autowired
    private MemberService memberService;

    @Autowired
    private FollowService followService;

    @DisplayName("내 정보 조회")
    @Test
    void getMyProfileTest() {
        Member member = signupAndLogin("testDisplayId", "testEmail@test.net");
        MemberResDto memberResDto = memberService.getMyProfile();

        assertThat(memberResDto.getDisplayId()).isEqualTo(member.getDisplayId());
        assertThat(memberResDto.getNickname()).isEqualTo(member.getNickname());
        assertThat(memberResDto.getProfileImageUrl()).isNull();
        assertThat(memberResDto.getIntroduction()).isNull();
        assertThat(memberResDto.getPostCount()).isEqualTo(member.getPostCount());
        assertThat(memberResDto.getFollowerCount()).isEqualTo(member.getFollowerCount());
        assertThat(memberResDto.getFollowingCount()).isEqualTo(member.getFollowingCount());
        assertThat(memberResDto.getIsFollowedByMe()).isFalse();
    }

    @DisplayName("다른 사람 정보 조회")
    @Nested
    class getOtherProfileTest {

        @DisplayName("팔로우 중인 사람 정보 조회")
        @Test
        void getFollowingProfileTest() {
            // given
            signupAndLogin("testDisplayId", "testEmail@test.net");
            Member other = signup("other", "other@test.net");

            // when
            followService.follow(other.getDisplayId());
            MemberResDto profile = memberService.getProfile(other.getDisplayId());

            // then
            assertThat(profile.getDisplayId()).isEqualTo(other.getDisplayId());
            assertThat(profile.getNickname()).isEqualTo(other.getNickname());
            assertThat(profile.getProfileImageUrl()).isNull();
            assertThat(profile.getIntroduction()).isNull();
            assertThat(profile.getPostCount()).isEqualTo(other.getPostCount());
            assertThat(profile.getFollowerCount()).isEqualTo(other.getFollowerCount());
            assertThat(profile.getFollowingCount()).isEqualTo(other.getFollowingCount());
            assertThat(profile.getIsFollowedByMe()).isTrue();
        }

        @DisplayName("팔로우 하지 않은 사람 정보 조회")
        @Test
        void getUnfollowingProfileTest() {
            // given
            signupAndLogin("testDisplayId", "testEmail@test.net");
            Member other = signup("other", "other@test.net");

            // when
            MemberResDto profile = memberService.getProfile(other.getDisplayId());

            // then
            assertThat(profile.getIsFollowedByMe()).isFalse();
        }
    }

    @DisplayName("activate 테스트")
    @Test
    void activate() {
        Member member = signupAndLogin("testDisplayId", "testEmail@test.net");

        CustomException ex = assertThrows(CustomException.class, () -> memberService.activate());
        assertEquals(ErrorCode.ALREADY_ACTIVATED, ex.getErrorCode());

        memberService.deactivate();
        memberService.activate();

        assertTrue(member.isActive());
    }

    @DisplayName("de-activate 테스트")
    @Test
    void deactivate() {
        Member member = signupAndLogin("testDisplayId", "testEmail@test.net");
        memberService.deactivate();
        assertFalse(member.isActive());

        CustomException ex = assertThrows(CustomException.class, () -> memberService.deactivate());
        assertEquals(ErrorCode.ALREADY_DEACTIVATED, ex.getErrorCode());
    }
}
