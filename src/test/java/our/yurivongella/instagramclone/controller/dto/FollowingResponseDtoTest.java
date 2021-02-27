package our.yurivongella.instagramclone.controller.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class FollowingResponseDtoTest {

    @Test
    public void dto_test() throws Exception {
        MemberResponseDto memberResponseDto = new MemberResponseDto("test");
        List<MemberResponseDto> memberResponseDtos = new ArrayList<>();
        memberResponseDtos.add(memberResponseDto);
        FollowingResponseDto followingResponseDto = new FollowingResponseDto();
        followingResponseDto.getFollowings().add(memberResponseDto);
        assertEquals(memberResponseDtos,followingResponseDto.getFollowings());
    }

}