package our.yurivongella.instagramclone.controller.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import our.yurivongella.instagramclone.domain.member.Member;

@Getter
@NoArgsConstructor
public class MemberDto {
    Long id;
    String name;
    @JsonProperty("image")
    String pictureUrl;
    Boolean following;

    @Builder
    public MemberDto(Long id, String name, String pictureUrl, Boolean following) {
        this.id = id;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.following = following;
    }

    public static MemberDto of(Member otherUser, Member user) {
        return MemberDto.builder()
                .id(otherUser.getId())
                .name(otherUser.getName())
                .pictureUrl(null)
                .following(otherUser.getFollowers().stream().anyMatch(v -> v.getFromMember().getId().equals(user.getId())))
                .build();
    }
}
