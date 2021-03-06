package our.yurivongella.instagramclone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import our.yurivongella.instagramclone.controller.dto.SigninRequestDto;
import our.yurivongella.instagramclone.controller.dto.SignupRequestDto;
import our.yurivongella.instagramclone.controller.dto.TokenDto;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.member.MemberRepository;
import our.yurivongella.instagramclone.jwt.TokenProvider;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String signup(SignupRequestDto signupRequestDto) {
        if (memberRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        Member member = signupRequestDto.toMember(passwordEncoder);
        return memberRepository.save(member).getEmail();
    }

    public TokenDto signin(SigninRequestDto signinRequestDto) {
        // 1. username, password 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = signinRequestDto.toAuthenticationToken();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        // authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. SecurityContext 에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 4. 인증 정보를 기반으로 JWT Access 토큰 생성
        String accessToken = tokenProvider.createAccessToken(authentication);

        return TokenDto.builder().accessToken(accessToken).build();
    }
}
