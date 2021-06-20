package our.yurivongella.instagramclone.controller.dto.member;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import our.yurivongella.instagramclone.entity.Member;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    @NotNull
    private String displayId;
    private String nickname;
    private String profileImageUrl;

    public static MemberResponseDto of(Member member) {
        return MemberResponseDto.builder()
                                .displayId(member.getDisplayId())
                                .nickname(member.getNickname())
                                .profileImageUrl(member.getProfileImageUrl())
                                .build();
    }
}
