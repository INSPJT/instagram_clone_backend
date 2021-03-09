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
    Boolean following;

    @Builder
    public MemberDto(Long id, String displayId, String profileImageUrl, Boolean following) {
        this.id = id;
        this.displayId = displayId;
        this.profileImageUrl = profileImageUrl;
        this.following = following;
    }

    public static MemberDto of(Member otherUser, Member user) {
        return builder()
                .id(otherUser.getId())
                .displayId(otherUser.getDisplayId())
                .following(otherUser.getFollowers()
                                    .stream()
                                    .anyMatch(v -> v.getFromMember().getId().equals(user.getId())))
                .build();
    }
}
