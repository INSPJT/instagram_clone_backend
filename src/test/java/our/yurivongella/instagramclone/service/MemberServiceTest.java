package our.yurivongella.instagramclone.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import our.yurivongella.instagramclone.controller.dto.MemberRequestDto;

@Transactional
@SpringBootTest
class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @DisplayName("가입하기")
    @Test
    public void signIn() throws Exception {
        MemberRequestDto memberRequestDto1 = new MemberRequestDto();
        memberRequestDto1.setName("test1");
        MemberRequestDto memberRequestDto2 = new MemberRequestDto();
        memberRequestDto2.setName("test2");
        MemberRequestDto memberRequestDto3 = new MemberRequestDto();
        memberRequestDto3.setName("test3");

        Long aLong = memberService.signUp(memberRequestDto1);
        Long aLong1 = memberService.signUp(memberRequestDto2);
        Long aLong2 = memberService.signUp(memberRequestDto3);

        boolean follow1 = memberService.follow(1L, 2L);
        boolean follow2 = memberService.follow(1L, 3L);
        boolean follow3 = memberService.follow(2L, 1L);
        boolean follow4 = memberService.follow(2L, 3L);
        boolean follow5 = memberService.follow(3L, 1L);
        boolean follow6 = memberService.follow(3L, 2L);

        assertEquals(1, aLong);
        assertEquals(2, aLong1);
        assertEquals(3, aLong2);
    }

}