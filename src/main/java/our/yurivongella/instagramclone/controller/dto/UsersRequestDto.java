package our.yurivongella.instagramclone.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import our.yurivongella.instagramclone.domain.Users.Users;

@NoArgsConstructor
@Data
public class UsersRequestDto {
    private String name;

    public Users toUsers(){
        return Users.builder()
                    .name(getName())
                    .build();
    }
}
