package our.yurivongella.instagramclone.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FollowRequestDto {
    private Long fromUser;
    private Long toUser;

    public FollowRequestDto() {
    }
}
