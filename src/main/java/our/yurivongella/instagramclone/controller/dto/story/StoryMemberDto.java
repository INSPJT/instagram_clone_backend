package our.yurivongella.instagramclone.controller.dto.story;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import our.yurivongella.instagramclone.domain.member.Member;

@Getter
@NoArgsConstructor
public class StoryMemberDto {
    String displayId;
    String nickname;
    String profileImageUrl;

    @Builder
    public StoryMemberDto (String displayId, String nickname, String  profileImageUrl) {
        this.displayId = displayId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    public static StoryMemberDto of(Member member) {
        return StoryMemberDto.builder()
                .displayId(member.getDisplayId())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
    }
}
