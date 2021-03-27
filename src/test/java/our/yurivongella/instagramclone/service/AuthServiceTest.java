package our.yurivongella.instagramclone.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import our.yurivongella.instagramclone.controller.dto.SigninRequestDto;
import our.yurivongella.instagramclone.controller.dto.SignupRequestDto;
import our.yurivongella.instagramclone.controller.dto.TokenDto;
import our.yurivongella.instagramclone.controller.dto.TokenRequestDto;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.member.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DisplayName("가입/로그인 테스트")
@SpringBootTest
@Transactional
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String displayId = "test";
    private final String nickname = "testNickname";
    private final String email = "authService1@test.net";
    private final String password = "1q2w3e4r";

    @BeforeEach
    public void signupBeforeTest() {
        // given
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .displayId(displayId)
                .nickname(nickname)
                .email(email)
                .password(password)
                .build();

        // when
        authService.signup(signupRequestDto);
    }

    @DisplayName("가입하기")
    @Nested
    class SignupTest {

        @DisplayName("성공")
        @Test
        public void successSignup() {
            // then
            Optional<Member> member = memberRepository.findByEmail(email);
            assertThat(member.isPresent()).isTrue();
            assertThat(member.get().getNickname()).isEqualTo(nickname);
            assertThat(member.get().getDisplayId()).isEqualTo(displayId);
            assertThat(member.get().getEmail()).isEqualTo(email);
            assertThat(
                    passwordEncoder.matches(password, member.get().getPassword())
            ).isTrue();
        }

        @DisplayName("중복 가입해서 실패")
        @Test
        public void failSignup() {
            SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                    .displayId("test2")
                    .nickname("testNickname2")
                    .email(email)
                    .password("1q2w3e4r5t")
                    .build();

            Assertions.assertThrows(
                    RuntimeException.class,
                    () -> authService.signup(signupRequestDto)
            );
        }
    }


    @DisplayName("로그인")
    @Nested
    class LoginTest {

        @DisplayName("로그인 성공")
        @Test
        public void successLogin() {
            // given
            SigninRequestDto signinRequestDto = SigninRequestDto.builder()
                    .email(email)
                    .password(password)
                    .build();

            // when
            TokenDto tokenDto = authService.signin(signinRequestDto);

            // then
            assertThat(tokenDto.getAccessToken()).isNotNull();
            assertThat(tokenDto.getRefreshToken()).isNotNull();
            assertThat(tokenDto.getGrantType()).isNotNull();
            assertThat(tokenDto.getAccessTokenExpiresIn()).isNotNull();
        }

        @DisplayName("이메일 불일치로 실패")
        @Test
        public void mismatchEmail() {
            // given
            SigninRequestDto signinRequestDto = SigninRequestDto.builder()
                    .email("mismatch" + email)
                    .password(password)
                    .build();

            // when
            Assertions.assertThrows(
                    RuntimeException.class,
                    () -> authService.signin(signinRequestDto)
            );
        }

        @DisplayName("비밀번호 불일치로 실패")
        @Test
        public void mismatchPassword() {
            // given
            SigninRequestDto signinRequestDto = SigninRequestDto.builder()
                    .email(email)
                    .password(password + "adfsdf")
                    .build();

            // when
            Assertions.assertThrows(
                    BadCredentialsException.class,
                    () -> authService.signin(signinRequestDto)
            );
        }
    }

    @DisplayName("토큰 재발급")
    @Nested
    class ReissueTest {

        private TokenDto tokenDto;

        @BeforeEach
        public void signinBeforeTest() {
            tokenDto = authService.signin(
                    SigninRequestDto.builder()
                            .email(email)
                            .password(password)
                            .build()
            );
        }


        @DisplayName("Access + Refresh 요청해서 성공")
        @Test
        public void accessRefresh() {
            // given
            TokenRequestDto tokenRequestDto = TokenRequestDto.builder()
                    .accessToken(tokenDto.getAccessToken())
                    .refreshToken(tokenDto.getRefreshToken())
                    .build();

            // when
            TokenDto reissue = authService.reissue(tokenRequestDto);

            // then
            boolean accessIsLongerThanRefresh = reissue.getAccessToken().length() > reissue.getRefreshToken().length();

            assertThat(accessIsLongerThanRefresh).isTrue();
            assertThat(reissue.getAccessToken()).isInstanceOf(String.class);
            assertThat(reissue.getRefreshToken()).isInstanceOf(String.class);
            assertThat(reissue.getGrantType()).isEqualTo("Bearer");
            assertThat(reissue.getAccessTokenExpiresIn()).isInstanceOf(Long.class);
        }

        @DisplayName("Access + Access 요청해서 실패")
        @Test
        public void accessAccess() {
            // given
            TokenRequestDto tokenRequestDto = TokenRequestDto.builder()
                    .accessToken(tokenDto.getAccessToken())
                    .refreshToken(tokenDto.getAccessToken())
                    .build();

            // when
            Assertions.assertThrows(
                    RuntimeException.class,
                    () -> authService.reissue(tokenRequestDto)
            );
        }

        @DisplayName("Refresh + Refresh 요청해서 실패")
        @Test
        public void refreshRefresh() {
            // given
            TokenRequestDto tokenRequestDto = TokenRequestDto.builder()
                    .accessToken(tokenDto.getRefreshToken())
                    .refreshToken(tokenDto.getRefreshToken())
                    .build();

            // when
            Assertions.assertThrows(
                    RuntimeException.class,
                    () -> authService.reissue(tokenRequestDto)
            );
        }

        @DisplayName("Refresh + Access 요청해서 실패")
        @Test
        public void refreshAccess() {
            // given
            TokenRequestDto tokenRequestDto = TokenRequestDto.builder()
                    .accessToken(tokenDto.getRefreshToken())
                    .refreshToken(tokenDto.getAccessToken())
                    .build();

            // when
            Assertions.assertThrows(
                    RuntimeException.class,
                    () -> authService.reissue(tokenRequestDto)
            );
        }
    }
}
