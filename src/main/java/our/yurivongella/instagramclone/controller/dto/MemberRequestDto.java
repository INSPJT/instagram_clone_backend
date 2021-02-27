package our.yurivongella.instagramclone.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import our.yurivongella.instagramclone.domain.member.Member;

@NoArgsConstructor
@Data
public class MemberRequestDto {
    private String name;

    public Member toMember(){
        return Member.builder()
                    .name(getName())
                    .build();
    }
}
