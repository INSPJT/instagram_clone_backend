package our.yurivongella.instagramclone.repository.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import our.yurivongella.instagramclone.entity.Post;

public interface CustomPostRepository {
    Slice<Post> findAllByJoinFollow(Long memberId, Pageable pageable);
}
