package our.yurivongella.instagramclone.controller.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import our.yurivongella.instagramclone.domain.member.Member;

class MemberRequestDtoTest {
    @Test
    public void dto_test() throws Exception {
        MemberRequestDto memberRequestDto = new MemberRequestDto();
        memberRequestDto.setName("test");
        Member member = memberRequestDto.toMember();
        assertEquals("test",member.getName());
    }
}