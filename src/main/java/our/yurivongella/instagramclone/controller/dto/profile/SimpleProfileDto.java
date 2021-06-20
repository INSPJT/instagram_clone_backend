package our.yurivongella.instagramclone.controller.dto.profile;

import our.yurivongella.instagramclone.domain.member.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleProfileDto {
    private String displayId;
    private String profileImageUrl;

    public static SimpleProfileDto of(Member member) {
        return SimpleProfileDto.builder()
                               .displayId(member.getDisplayId())
                               .profileImageUrl(member.getProfileImageUrl())
                               .build();
    }
}
