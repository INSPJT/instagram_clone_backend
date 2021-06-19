package our.yurivongella.instagramclone.controller.dto.profile;

import our.yurivongella.instagramclone.domain.member.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {
    private ProfileMemberDto memberDto;
    private Long postCount;
    private Long followerCount;
    private Long followingCount;

    public static ProfileDto of(Member member) {
        return ProfileDto.builder()
                .memberDto(ProfileMemberDto.of(member))
                .postCount(member.getPostCount())
                .followerCount(member.getFollowerCount())
                .followingCount(member.getFollowingCount())
                .build();
    }
}
