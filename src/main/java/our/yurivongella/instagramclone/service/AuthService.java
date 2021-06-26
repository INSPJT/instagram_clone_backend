package our.yurivongella.instagramclone.service;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import our.yurivongella.instagramclone.controller.dto.member.SigninReqDto;
import our.yurivongella.instagramclone.controller.dto.member.SignupReqDto;
import our.yurivongella.instagramclone.controller.dto.member.token.TokenDto;
import our.yurivongella.instagramclone.controller.dto.member.token.TokenReqDto;
import our.yurivongella.instagramclone.entity.Member;
import our.yurivongella.instagramclone.repository.MemberRepository;
import our.yurivongella.instagramclone.entity.RefreshToken;
import our.yurivongella.instagramclone.repository.RefreshTokenRepository;
import our.yurivongella.instagramclone.exception.CustomException;
import our.yurivongella.instagramclone.jwt.TokenProvider;

import static our.yurivongella.instagramclone.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public String signup(SignupReqDto signupReqDto) {
        checkDisplayId(signupReqDto.getDisplayId());
        checkEmail(signupReqDto.getEmail());
        Member member = signupReqDto.toMember(passwordEncoder);
        return memberRepository.save(member).getEmail();
    }

    @Transactional
    public TokenDto signin(SigninReqDto signinReqDto) {
        // 1. username, password 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = signinReqDto.toAuthenticationToken();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                                                .key(authentication.getName())
                                                .value(tokenDto.getRefreshToken())
                                                .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenReqDto tokenReqDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenReqDto.getRefreshToken())) {
            throw new CustomException(INVALID_REFRESH_TOKEN);
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenReqDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                                                          .orElseThrow(() -> new CustomException(REFRESH_TOKEN_NOT_FOUND));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenReqDto.getRefreshToken())) {
            throw new CustomException(MISMATCH_REFRESH_TOKEN);
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        refreshToken.updateValue(tokenDto.getRefreshToken());

        // 토큰 발급
        return tokenDto;
    }

    public boolean validate(String target) {
        return target.contains("@") ? checkEmail(target) : checkDisplayId(target);
    }

    private boolean checkDisplayId(String displayId) {
        memberRepository.findByDisplayId(displayId).ifPresent(ignored -> {
            throw new CustomException(ALREADY_EXISTS_DISPLAY_ID);
        });
        return true;
    }

    private boolean checkEmail(String email) {
        memberRepository.findByEmail(email).ifPresent(ignored -> {
            throw new CustomException(ALREADY_EXISTS_EMAIL);
        });
        return true;
    }
}
