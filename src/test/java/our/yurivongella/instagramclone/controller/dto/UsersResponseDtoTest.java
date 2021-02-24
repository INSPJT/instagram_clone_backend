package our.yurivongella.instagramclone.controller.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UsersResponseDtoTest {
    @Test
    public void dto_test() throws Exception {
        UsersResponseDto usersResponseDto = new UsersResponseDto("test");
        assertEquals("test",usersResponseDto.getName());
    }
}