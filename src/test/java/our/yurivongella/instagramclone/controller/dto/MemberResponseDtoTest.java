package our.yurivongella.instagramclone.controller.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import our.yurivongella.instagramclone.domain.member.Member;

import static org.assertj.core.api.Assertions.*;

@DisplayName("MemberResponseDtoTest")
class MemberResponseDtoTest {

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
        MemberResponseDto memberResponseDto = MemberResponseDto.of(member);

        assertThat(memberResponseDto.getDisplayId()).isEqualTo(displayId);
        assertThat(memberResponseDto.getNickname()).isEqualTo(nickname);
        assertThat(memberResponseDto.getEmail()).isEqualTo(email);
        assertThat(memberResponseDto.getProfileImageUrl()).isEqualTo(profileImageUrl);
        assertThat(memberResponseDto.getId()).isNull();
    }
}