package our.yurivongella.instagramclone.controller.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FollowerResponseDto {
    private List<UsersResponseDto> followers = new ArrayList<>();
}
