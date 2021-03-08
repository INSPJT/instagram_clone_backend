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
    String mediaUrl;
    Boolean following;

    @Builder
    public MemberDto(Long id, String name, String mediaUrl, Boolean following) {
        this.id = id;
        this.name = name;
        this.mediaUrl = mediaUrl;
        this.following = following;
    }

    public static MemberDto of(Member otherUser, Member user) {
        return builder()
                .id(otherUser.getId())
                .name(otherUser.getName())
                .following(otherUser.getFollowers()
                                    .stream()
                                    .anyMatch(v -> v.getFromMember().getId().equals(user.getId())))
                .build();
    }
}
