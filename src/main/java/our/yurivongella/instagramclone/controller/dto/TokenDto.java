package our.yurivongella.instagramclone.controller.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    private String accessToken;
    private LocalDateTime expiresIn;
    private String refreshToken;
    private LocalDateTime refreshTokenExpiresIn;
}
