package our.yurivongella.instagramclone.controller.dto.member;

import our.yurivongella.instagramclone.entity.Member;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupReqDto {

    @NotNull
    private String displayId;

    @NotNull
    private String email;

    @NotNull
    private String password;

    private String nickname;

    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                     .displayId(displayId)
                     .email(email)
                     .password(passwordEncoder.encode(password))
                     .nickname(nickname)
                     .build();
    }
}
