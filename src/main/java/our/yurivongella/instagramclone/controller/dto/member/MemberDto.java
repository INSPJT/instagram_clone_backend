package our.yurivongella.instagramclone.controller.dto.member;

import our.yurivongella.instagramclone.entity.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class MemberDto {
    private String displayId;
    private String nickname;
    private String profileImageUrl;
    private Boolean isFollowedByMe;

    @Builder
    public MemberDto(final String displayId, final String nickname, final String profileImageUrl, final Boolean isFollowedByMe) {
        this.displayId = displayId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.isFollowedByMe = isFollowedByMe;
    }

    public static MemberDto of(Member other, Member currentMember) {
        return builder()
                .displayId(other.getDisplayId())
                .nickname(other.getNickname())
                .profileImageUrl(other.getProfileImageUrl())
                .isFollowedByMe(other.isFollowedBy(currentMember))
                .build();
    }
}
