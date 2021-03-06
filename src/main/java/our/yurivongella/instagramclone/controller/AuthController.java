package our.yurivongella.instagramclone.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import our.yurivongella.instagramclone.controller.dto.SigninRequestDto;
import our.yurivongella.instagramclone.controller.dto.TokenDto;
import our.yurivongella.instagramclone.controller.dto.SignupRequestDto;
import our.yurivongella.instagramclone.service.AuthService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto signupRequestDto) {
        String email = authService.signup(signupRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(email + " 회원가입이 완료되었습니다.");
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenDto> signin(@RequestBody SigninRequestDto signinRequestDto) {
        TokenDto tokenDto = authService.signin(signinRequestDto);
        return ResponseEntity.ok(tokenDto);
    }
}
