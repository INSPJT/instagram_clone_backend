package our.yurivongella.instagramclone.controller.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class FollowingResponseDtoTest {

    @Test
    public void dto_test() throws Exception {
        UsersResponseDto usersResponseDto = new UsersResponseDto("test");
        List<UsersResponseDto> usersResponseDtos = new ArrayList<>();
        usersResponseDtos.add(usersResponseDto);
        FollowingResponseDto followingResponseDto = new FollowingResponseDto();
        followingResponseDto.getFollowings().add(usersResponseDto);
        assertEquals(usersResponseDtos,followingResponseDto.getFollowings());
    }

}