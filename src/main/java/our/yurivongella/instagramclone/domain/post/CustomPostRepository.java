package our.yurivongella.instagramclone.domain.post;

import java.util.List;

public interface CustomPostRepository {
    List<Post> findAllByJoinFollow(Long memberId, Long lastId, int pageSize);
}
