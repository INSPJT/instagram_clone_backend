package our.yurivongella.instagramclone.controller.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import our.yurivongella.instagramclone.controller.dto.member.MemberDto;
import our.yurivongella.instagramclone.entity.Follow;
import our.yurivongella.instagramclone.entity.Member;

import static org.assertj.core.api.Assertions.*;

public class MemberDtoTest {

    private final static String PREFIX = "memberdtotest";

    @DisplayName("Other + Member -> MemberDto")
    @Test
    public void otherAndMemberToMemberDto() {
        // given
        Member member = Member.builder()
                              .displayId(PREFIX + "current")
                              .email(PREFIX + "current@test.net")
                              .password(PREFIX + "current")
                              .build();

        Member other = Member.builder()
                .displayId(PREFIX + "other")
                .email(PREFIX + "other@test.net")
                .password(PREFIX + "other")
                .build();

        Follow follow = Follow.builder()
                              .fromMember(member)
                              .toMember(other)
                              .build();

        other.getFollowers().add(follow);
        member.getFollowings().add(follow);

        // when
        MemberDto memberDto = MemberDto.of(other, member);

        // then
        assertThat(memberDto.getDisplayId()).isEqualTo(PREFIX + "other");
        assertThat(memberDto.getProfileImageUrl()).isEqualTo(null);
        assertThat(memberDto.getIsFollowedByMe()).isEqualTo(true);
    }
}
