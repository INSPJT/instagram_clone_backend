package our.yurivongella.instagramclone.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import our.yurivongella.instagramclone.controller.dto.auth.SignupReqDto;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.member.MemberRepository;
import our.yurivongella.instagramclone.exception.CustomException;
import our.yurivongella.instagramclone.exception.ErrorCode;

@Transactional
@SpringBootTest
class MemberServiceTest {
    @MockBean
    private S3Service s3Service;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthService authService;

    private Long memberId;

    @BeforeEach
    public void signupBeforeTest() {
        final String displayId = "test";
        final String nickname = "testNickname";
        final String email = "authService1@test.net";
        final String password = "1q2w3e4r";
        SignupReqDto signupReqDto = SignupReqDto.builder()
                                                            .displayId(displayId)
                                                            .nickname(nickname)
                                                            .email(email)
                                                            .password(password)
                                                            .build();

        // 가입
        authService.signup(signupReqDto);
        memberId = memberRepository.findByEmail(email).get().getId();

        Authentication authentication = new UsernamePasswordAuthenticationToken(memberId, "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @DisplayName("activate 테스트")
    @Test
    void activate() {
        CustomException customException = assertThrows(CustomException.class, () -> memberService.activate());
        assertEquals(ErrorCode.ALREADY_ACTIVATED, customException.getErrorCode());

        memberService.deactivate();
        memberService.activate();
        Member member = memberRepository.findById(memberId).get();

        assertTrue(member.isActive());
    }

    @DisplayName("de-activate 테스트")
    @Test
    void deactivate() {
        memberService.deactivate();
        Member member = memberRepository.findById(memberId).get();
        assertFalse(member.isActive());

        CustomException customException = assertThrows(CustomException.class, () -> memberService.deactivate());
        assertEquals(ErrorCode.ALREADY_DEACTIVATED, customException.getErrorCode());
    }

    @DisplayName("delete 테스트")
    @Test
    void delete() {
        memberService.delete();
        Optional<Member> findMember = memberRepository.findById(memberId);
        assertEquals(Optional.empty(), findMember);
    }
}