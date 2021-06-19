package our.yurivongella.instagramclone.controller.dto.member.token;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequestDto {

    @NotNull
    private String accessToken;

    @NotNull
    private String refreshToken;
}
