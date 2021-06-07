package our.yurivongella.instagramclone.domain.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomPostRepository {
    Slice<Post> findAllByJoinFollow(Long memberId, Pageable pageable);
}
