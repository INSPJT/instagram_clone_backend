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
@SpringBootTest
@Transactional
public class CustomUserDetailsServiceTest {
    @MockBean
    private S3Service s3Service;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("이메일로 유저 정보 가져오기")
    @Test
    public void loadUserByUsernameTest() {
        // given
        String displayId = "test";
        String nickname = "testNickname";
        String email = "customUserDetailsService@test.net";
        String password = "1q2w3e4r";

        Member member = Member.builder()
                              .displayId(displayId)
                              .nickname(nickname)
                              .email(email)
                              .password(password)
                              .build();

        // when
        memberRepository.save(member);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        // then
        Member foundMember = memberRepository.findByEmail(email).get();
        assertThat(userDetails.getUsername()).isEqualTo(foundMember.getId().toString());
        assertThat(memberRepository.findByEmail(email).isPresent()).isTrue();
    }
}
