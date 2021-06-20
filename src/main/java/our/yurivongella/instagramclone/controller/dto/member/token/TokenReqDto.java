package our.yurivongella.instagramclone.controller.dto.member.token;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenReqDto {

    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;
}
