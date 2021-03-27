package our.yurivongella.instagramclone.controller.dto.post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import our.yurivongella.instagramclone.domain.member.Member;

@Getter
@NoArgsConstructor
public class MemberDto {
    Long id;
    String displayId;
    String profileImageUrl;
    Boolean isFollowedByMe;

    @Builder
    public MemberDto(Long id, String displayId, String profileImageUrl, Boolean isFollowedByMe) {
        this.id = id;
        this.displayId = displayId;
        this.profileImageUrl = profileImageUrl;
        this.isFollowedByMe = isFollowedByMe;
    }

    public static MemberDto of(Member other, Member currentMember) {
        return builder()
                .id(other.getId())
                .displayId(other.getDisplayId())
                .profileImageUrl(other.getProfileImageUrl())
                .isFollowedByMe(other.isFollowedBy(currentMember))
                .build();
    }
}
