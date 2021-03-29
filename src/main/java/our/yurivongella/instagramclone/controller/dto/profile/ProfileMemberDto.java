package our.yurivongella.instagramclone.controller.dto.profile;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import our.yurivongella.instagramclone.domain.member.Member;

@Getter
@NoArgsConstructor
public class ProfileMemberDto {
    private String nickname;
    private String displayId;
    private String profileImageUrl;
    private String introduction;
    private Boolean isFollow;

    @Builder
    public ProfileMemberDto(String nickname, String displayId, String profileImageUrl, String introduction) {
        this.nickname = nickname;
        this.displayId = displayId;
        this.profileImageUrl = profileImageUrl;
        this.introduction = introduction;
        this.isFollow = false;
    }

    public static ProfileMemberDto of(Member member) {
        return ProfileMemberDto.builder()
                .nickname(member.getNickname())
                .displayId(member.getDisplayId())
                .profileImageUrl(member.getProfileImageUrl())
                .introduction(member.getIntroduction())
                .build();
    }

    public void setFollowTrue() {
        this.isFollow = true;
    }
}

