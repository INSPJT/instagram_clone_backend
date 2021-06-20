package our.yurivongella.instagramclone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import our.yurivongella.instagramclone.controller.dto.story.MyStoryDto;
import our.yurivongella.instagramclone.controller.dto.story.StoryMemberDto;
import our.yurivongella.instagramclone.domain.story.StoryRepository;
import our.yurivongella.instagramclone.util.SecurityUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoryService {
    StoryRepository storyRepository;

    public List<MyStoryDto> getMyStories() {
        return storyRepository.findAllByMemberId(SecurityUtil.getCurrentMemberId()).stream()
                .map((story)->MyStoryDto.of(story, StoryMemberDto.of(story.getMember()),story.getMediaUrl()))
                .collect(Collectors.toList());
    }
}
