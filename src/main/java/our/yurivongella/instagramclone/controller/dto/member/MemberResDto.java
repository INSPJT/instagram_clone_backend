package our.yurivongella.instagramclone.controller.dto.member;

import javax.validation.constraints.NotNull;

import our.yurivongella.instagramclone.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResDto {
    @NotNull
    private String displayId;
    private String nickname;
    private String profileImageUrl;

    public static MemberResDto of(Member member) {
        return MemberResDto.builder()
                           .displayId(member.getDisplayId())
                           .nickname(member.getNickname())
                           .profileImageUrl(member.getProfileImageUrl())
                           .build();
    }
}
