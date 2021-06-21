package our.yurivongella.instagramclone.controller.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import our.yurivongella.instagramclone.controller.dto.member.MemberResDto;
import our.yurivongella.instagramclone.entity.Member;

import static org.assertj.core.api.Assertions.*;

@DisplayName("MemberResponseDtoTest")
class MemberResDtoTest {

    @Test
    @DisplayName("Member -> MemberResponseDto")
    public void memberResponseDtoTest() {
        // given
        String displayId = "test";
        String nickname = "testNickname";
        String email = "test@test.net";
        String password = "1q2w3e4r";
        String profileImageUrl = "test.img";

        Member member = Member.builder()
                              .displayId(displayId)
                              .nickname(nickname)
                              .email(email)
                              .password(password)
                              .profileImageUrl(profileImageUrl)
                              .build();

        // when
        MemberResDto memberResDto = MemberResDto.of(member);

        assertThat(memberResDto.getDisplayId()).isEqualTo(displayId);
        assertThat(memberResDto.getNickname()).isEqualTo(nickname);
        assertThat(memberResDto.getProfileImageUrl()).isEqualTo(profileImageUrl);
    }
}
