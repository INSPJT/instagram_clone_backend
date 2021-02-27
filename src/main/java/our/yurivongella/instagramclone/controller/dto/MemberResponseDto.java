package our.yurivongella.instagramclone.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberResponseDto {
    private String name;

    public MemberResponseDto(String name) {
        this.name = name;
    }
}
