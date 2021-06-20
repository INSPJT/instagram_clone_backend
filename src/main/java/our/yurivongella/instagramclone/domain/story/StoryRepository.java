package our.yurivongella.instagramclone.domain.story;

import org.springframework.data.jpa.repository.JpaRepository;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.refeshtoken.RefreshToken;

import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long> {
    List<Story> findAllByMemberId(Long memberId);
}
