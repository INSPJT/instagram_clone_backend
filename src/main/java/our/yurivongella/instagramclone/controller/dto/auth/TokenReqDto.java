package our.yurivongella.instagramclone.controller.dto.auth;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenReqDto {

    @NotNull
    private String accessToken;

    @NotNull
    private String refreshToken;
}