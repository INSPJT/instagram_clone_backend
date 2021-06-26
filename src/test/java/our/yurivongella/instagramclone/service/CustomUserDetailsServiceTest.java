package our.yurivongella.instagramclone.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import our.yurivongella.instagramclone.entity.Member;
import our.yurivongella.instagramclone.repository.MemberRepository;

import static org.assertj.core.api.Assertions.*;

@DisplayName("UserDetailsService 테스트")
public class CustomUserDetailsServiceTest extends TestBase {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @DisplayName("이메일로 유저 정보 가져오기")
    @Test
    public void loadUserByUsernameTest() {
        // given
        final String EMAIL = "test@test.net";
        signup("test", EMAIL);

        // when
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(EMAIL);

        // then
        Member member = memberRepository.findByEmail(EMAIL).get();
        assertThat(userDetails.getUsername()).isEqualTo(member.getId().toString());
    }
}
