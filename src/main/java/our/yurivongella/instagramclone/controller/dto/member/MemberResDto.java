package our.yurivongella.instagramclone.controller.dto.member;

import javax.validation.constraints.NotNull;

import our.yurivongella.instagramclone.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResDto {
    @NotNull
    private String displayId;
    private String nickname;
    private String profileImageUrl;
    private String introduction;
    private Long postCount;
    private Long followingCount;
    private Long followerCount;

    private Boolean isFollowedByMe; // 요청 보낸 계정이 팔로우 중인지

    public static MemberResDto of(Member member) {
        return MemberResDto.builder()
                .displayId(member.getDisplayId())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .introduction(member.getIntroduction())
                .postCount(member.getPostCount())
                .followingCount(member.getFollowingCount())
                .followerCount(member.getFollowerCount())
                .isFollowedByMe(false)
                .build();
    }

    public void setFollowTrue() {
        this.isFollowedByMe = true;
    }
}
