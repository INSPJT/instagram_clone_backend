package our.yurivongella.instagramclone.controller.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import our.yurivongella.instagramclone.domain.member.Member;

@ToString
@Getter
@NoArgsConstructor
public class MemberDto {
    String displayId;
    String nickname;
    String profileImageUrl;
    Boolean isFollowedByMe;

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
