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
        String name = "test";
        String nickName = "testNickname";
        String email = "test@test.net";
        String password = "1q2w3e4r";

        Member member = Member.builder()
                .name(name)
                .nickName(nickName)
                .email(email)
                .password(password)
                .build();

        // when
        MemberResponseDto memberResponseDto = MemberResponseDto.of(member);

        assertThat(memberResponseDto.getName()).isEqualTo(name);
        assertThat(memberResponseDto.getNickName()).isEqualTo(nickName);
        assertThat(memberResponseDto.getEmail()).isEqualTo(email);
        assertThat(memberResponseDto.getId()).isNull();
    }
}