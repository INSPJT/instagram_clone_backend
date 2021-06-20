package our.yurivongella.instagramclone.controller.dto.story;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import our.yurivongella.instagramclone.controller.dto.MediaUrlDto;
import our.yurivongella.instagramclone.domain.post.MediaUrl;
import our.yurivongella.instagramclone.domain.story.Story;

@NoArgsConstructor
@Getter
@Builder
public class MyStoryDto {
    private Long storyId;
    private StoryMemberDto storyMemberDto;
    private MediaUrlDto mediaUrlDto;
    private String createdAt;

    public static MyStoryDto of(Story story, StoryMemberDto storyMemberDto, MediaUrl mediaUrl) {
        return MyStoryDto.builder()
                .storyId(story.getId())
                .mediaUrlDto(MediaUrlDto.of(mediaUrl))
                .createdAt(story.getCreatedDate().toString())
                .build();
    }
}
