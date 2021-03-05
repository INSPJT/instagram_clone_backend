package our.yurivongella.instagramclone.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.member.MemberRepository;

import static org.assertj.core.api.Assertions.*;

@DisplayName("UserDetailsService 테스트")
@SpringBootTest
public class CustomUserDetailsServiceTest {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("이메일로 유저 정보 가져오기")
    @Test
    public void loadUserByUsernameTest() {
        // given
        String name = "test";
        String nickName = "testNickname";
        String email = "test@test.net";
        String password = "1q2w3e4r";

        Member member = Member.builder()
                .name(name)
                .nickName(nickName)
                .email(email)
                .password(password)
                .build();

        // when
        memberRepository.save(member);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        // then
        assertThat(userDetails.getUsername()).isEqualTo("1");
        assertThat(memberRepository.findByEmail(email).isPresent()).isTrue();
    }
}
