package our.yurivongella.instagramclone.repository.post;

import our.yurivongella.instagramclone.entity.Post;

import java.util.List;

public interface CustomPostRepository {
    List<Post> findAllByJoinFollow(Long memberId, Long lastId, int pageSize);
}
