package our.yurivongella.instagramclone.controller.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import our.yurivongella.instagramclone.domain.member.Users;

class UsersRequestDtoTest {
    @Test
    public void dto_test() throws Exception {
        UsersRequestDto usersRequestDto = new UsersRequestDto();
        usersRequestDto.setName("test");
        Users users = usersRequestDto.toUsers();
        assertEquals("test",users.getName());
    }
}