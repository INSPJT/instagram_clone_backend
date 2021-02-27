package our.yurivongella.instagramclone.controller.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import our.yurivongella.instagramclone.domain.Users.Users;

@Getter
@NoArgsConstructor
public class UsersDto {
    Long id;
    String name;
    @JsonProperty("image")
    String pictureUrl;
    Boolean following;

    @Builder
    public UsersDto(Long id, String name, String pictureUrl, Boolean following) {
        this.id = id;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.following = following;
    }

    public static UsersDto toUsersDto(Users otherUser, Users user) {
        return UsersDto.builder()
                .id(otherUser.getId())
                .name(otherUser.getName())
                .pictureUrl(null)
                .following(otherUser.getFollowers().stream().anyMatch(v -> v.getFromUser().getId().equals(user.getId())))
                .build();
    }
}
