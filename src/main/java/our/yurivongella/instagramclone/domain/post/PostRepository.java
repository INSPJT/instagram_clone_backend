package our.yurivongella.instagramclone.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import our.yurivongella.instagramclone.controller.dto.post.PostReadResponseDto;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllByMemberId(Long memberId);
}
