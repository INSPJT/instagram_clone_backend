package our.yurivongella.instagramclone.service;

import org.junit.jupiter.api.*;

import org.springframework.security.authentication.BadCredentialsException;
import our.yurivongella.instagramclone.controller.dto.member.SigninReqDto;
import our.yurivongella.instagramclone.controller.dto.member.token.TokenDto;
import our.yurivongella.instagramclone.controller.dto.member.token.TokenReqDto;
import our.yurivongella.instagramclone.entity.Member;
import our.yurivongella.instagramclone.exception.CustomException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static our.yurivongella.instagramclone.exception.ErrorCode.*;

@DisplayName("가입/로그인 테스트")
public class AuthServiceTest extends TestBase {

    private static final String PASSWORD = "1q2w3e4r";

    @DisplayName("가입하기")
    @Nested
    class SignupTest {

        @DisplayName("성공")
        @Test
        public void successSignup() {
            // given
            signup("testAuth", "testAuth@test.net");

            // then
            Member member = memberRepository.findByEmail("testAuth@test.net").get();
            assertThat(member.getNickname()).isEqualTo("test nickname");
            assertThat(member.getDisplayId()).isEqualTo("testAuth");
            assertThat(member.getEmail()).isEqualTo("testAuth@test.net");
            assertThat(passwordEncoder.matches(PASSWORD, member.getPassword())).isTrue();
        }

        @DisplayName("중복 가입해서 실패")
        @Test
        public void failSignup() {
            signup("testAuth", "testAuth@test.net");

            CustomException ex = assertThrows(CustomException.class, () -> signup("testAuth", "testAuth@test.net"));
            Assertions.assertEquals(ALREADY_EXISTS_DISPLAY_ID, ex.getErrorCode());
        }
    }

    @DisplayName("로그인")
    @Nested
    class LoginTest {

        @DisplayName("로그인 성공")
        @Test
        public void successLogin() {
            // given
            signup("testAuth", "testAuth@test.net");
            SigninReqDto signinReqDto = new SigninReqDto("testAuth@test.net", PASSWORD);

            // when
            TokenDto tokenDto = authService.signin(signinReqDto);

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
            signup("testAuth", "testAuth@test.net");
            SigninReqDto signinReqDto = new SigninReqDto("mismatch", PASSWORD);

            // when
            assertThatExceptionOfType(BadCredentialsException.class)
                    .isThrownBy(() -> authService.signin(signinReqDto));
        }

        @DisplayName("비밀번호 불일치로 실패")
        @Test
        public void mismatchPassword() {
            // given
            signup("testAuth", "testAuth@test.net");
            SigninReqDto signinReqDto = new SigninReqDto("testAuth@test.net", "mismatch");

            // when
            assertThatExceptionOfType(BadCredentialsException.class)
                    .isThrownBy(() -> authService.signin(signinReqDto));
        }
    }

    @DisplayName("토큰 재발급")
    @Nested
    class ReissueTest {

        private TokenDto tokenDto;

        @BeforeEach
        public void signinBeforeTest() {
            signup("testAuth", "testAuth@test.net");

            tokenDto = authService.signin(
                    SigninReqDto.builder()
                                .email("testAuth@test.net")
                                .password(PASSWORD)
                                .build()
            );
        }

        @DisplayName("Access + Refresh 요청해서 성공")
        @Test
        public void accessRefresh() {
            // given
            TokenReqDto tokenReqDto = TokenReqDto.builder()
                                                 .accessToken(tokenDto.getAccessToken())
                                                 .refreshToken(tokenDto.getRefreshToken())
                                                 .build();

            // when
            TokenDto reissue = authService.reissue(tokenReqDto);

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
            TokenReqDto tokenReqDto = TokenReqDto.builder()
                                                 .accessToken(tokenDto.getAccessToken())
                                                 .refreshToken(tokenDto.getAccessToken())
                                                 .build();

            // when
            CustomException ex = Assertions.assertThrows(CustomException.class, () -> authService.reissue(tokenReqDto));
            assertEquals(MISMATCH_REFRESH_TOKEN, ex.getErrorCode());
        }

        @DisplayName("Refresh + Refresh 요청해서 실패")
        @Test
        public void refreshRefresh() {
            // given
            TokenReqDto tokenReqDto = TokenReqDto.builder()
                                                 .accessToken(tokenDto.getRefreshToken())
                                                 .refreshToken(tokenDto.getRefreshToken())
                                                 .build();

            // when
            CustomException ex = Assertions.assertThrows(CustomException.class, () -> authService.reissue(tokenReqDto));
            assertEquals(INVALID_AUTH_TOKEN, ex.getErrorCode());
        }

        @DisplayName("Refresh + Access 요청해서 실패")
        @Test
        public void refreshAccess() {
            // given
            TokenReqDto tokenReqDto = TokenReqDto.builder()
                                                 .accessToken(tokenDto.getRefreshToken())
                                                 .refreshToken(tokenDto.getAccessToken())
                                                 .build();

            // when
            CustomException ex = Assertions.assertThrows(CustomException.class, () -> authService.reissue(tokenReqDto));
            assertEquals(INVALID_AUTH_TOKEN, ex.getErrorCode());
        }
    }

    @DisplayName("중복 검사")
    @Nested
    class ValidateResource {

        @DisplayName("검사 요청을 한 이메일이 DB 에 존재하지 않을 때")
        @Test
        void check_email_success() {
            boolean validate = authService.validate("testAuth@test.net");
            Assertions.assertTrue(validate);
        }

        @DisplayName("검사 요청을 한 이메일이 DB 에 이미 존재할 때")
        @Test
        void check_email_fail() {
            signup("testAuth", "testAuth@test.net");
            CustomException ex = Assertions.assertThrows(CustomException.class, () -> authService.validate("testAuth@test.net"));
            assertEquals(ALREADY_EXISTS_EMAIL, ex.getErrorCode());
        }

        @DisplayName("검사 요청을 한 displayId 가 DB에 존재하지 않을 때")
        @Test
        void check_displayId_success() {
            boolean validate = authService.validate("testAuth");
            Assertions.assertTrue(validate);
        }

        @DisplayName("검사 요청을 한 displayId 가 DB 에 이미 존재할 때")
        @Test
        void check_displayId_fail() {
            signup("testAuth", "testAuth@test.net");
            CustomException ex = Assertions.assertThrows(CustomException.class, () -> authService.validate("testAuth"));
            assertEquals(ALREADY_EXISTS_DISPLAY_ID, ex.getErrorCode());
        }
    }
}
