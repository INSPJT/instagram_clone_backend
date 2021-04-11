package our.yurivongella.instagramclone.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sun.istack.NotNull;

import lombok.extern.slf4j.Slf4j;
import our.yurivongella.instagramclone.controller.dto.SigninRequestDto;
import our.yurivongella.instagramclone.controller.dto.TokenDto;
import our.yurivongella.instagramclone.controller.dto.SignupRequestDto;
import our.yurivongella.instagramclone.controller.dto.TokenRequestDto;
import our.yurivongella.instagramclone.controller.dto.comment.ProcessStatus;
import our.yurivongella.instagramclone.service.AuthService;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @ApiOperation("가입하기")
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto signupRequestDto) {
        String email = authService.signup(signupRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(email + " 회원가입이 완료되었습니다.");
    }

    @ApiOperation("로그인")
    @PostMapping("/signin")
    public ResponseEntity<TokenDto> signin(@RequestBody SigninRequestDto signinRequestDto) {
        TokenDto tokenDto = authService.signin(signinRequestDto);
        return ResponseEntity.ok(tokenDto);
    }

    @ApiOperation("로그인 토큰 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

    @ApiOperation("중복 체크")
    @PutMapping("/check")
    public ResponseEntity<Boolean> check(@RequestParam(value = "target") @NotNull String target) {
        return ResponseEntity.ok(authService.validate(target));
    }

    @ApiOperation("회원 비활성화")
    @DeleteMapping("/deactivate")
    public ResponseEntity<ProcessStatus> deactivate(){
        return ResponseEntity.ok(authService.deactivate());
    }
}
