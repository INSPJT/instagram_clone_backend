package our.yurivongella.instagramclone.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import our.yurivongella.instagramclone.domain.member.Member;

@NoArgsConstructor
@Data
public class UsersRequestDto {
    private String name;

    public Member toUsers(){
        return Member.builder()
                    .name(getName())
                    .build();
    }
}
