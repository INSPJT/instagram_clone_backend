package our.yurivongella.instagramclone.controller.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowingResponseDto {
    private List<MemberResponseDto> followings = new ArrayList<>();
}
