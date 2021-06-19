package our.yurivongella.instagramclone.controller.dto.member;

import our.yurivongella.instagramclone.domain.member.Member;
import com.sun.istack.NotNull;

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
