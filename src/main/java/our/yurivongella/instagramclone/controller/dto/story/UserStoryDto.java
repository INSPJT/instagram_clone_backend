package our.yurivongella.instagramclone.controller.dto.story;

import our.yurivongella.instagramclone.domain.story.Story;

import java.util.HashMap;
import java.util.List;

public class UserStoryDto {
    private Integer unReadStoryCnt;
    private StoryMemberDto storyMemberDto;
    private List<Story> storyList;
}
