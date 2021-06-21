package our.yurivongella.instagramclone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import our.yurivongella.instagramclone.controller.dto.member.SignupReqDto;
import our.yurivongella.instagramclone.entity.Member;
import our.yurivongella.instagramclone.repository.MemberRepository;

import java.util.Collections;

@SpringBootTest
public abstract class TestBase {
    @MockBean
    private S3Service s3Service;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /* 테스트 계정 가입하기 */
    public Member signup(String displayId, String email) {
        SignupReqDto signupReqDto = SignupReqDto.builder()
                                                .displayId(displayId)
                                                .email(email)
                                                .password("1q2w3e4r")
                                                .nickname("test nickname")
                                                .build();

        Member member = signupReqDto.toMember(passwordEncoder);
        memberRepository.save(member);
        return member;
    }

    /* 테스트 계정 로그인 시키기 */
    public void login(Long memberId) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberId, "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /* 테스트 계정 가입 및 로그인 */
    public Member signupAndLogin(String displayId, String email) {
        Member member = signup(displayId, email);
        login(member.getId());
        return member;
    }
}
