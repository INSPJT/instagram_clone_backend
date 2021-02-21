package our.yurivongella.instagramclone.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UsersResponseDto {
    private String name;

    public UsersResponseDto(String name) {
        this.name = name;
    }
}
