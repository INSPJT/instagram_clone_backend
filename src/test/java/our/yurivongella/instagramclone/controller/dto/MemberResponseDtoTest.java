package our.yurivongella.instagramclone.controller.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MemberResponseDtoTest {
    @Test
    public void dto_test() throws Exception {
        MemberResponseDto memberResponseDto = new MemberResponseDto("test");
        assertEquals("test", memberResponseDto.getName());
    }
}