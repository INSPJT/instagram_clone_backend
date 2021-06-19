package our.yurivongella.instagramclone.domain.comment;

import java.util.List;

import our.yurivongella.instagramclone.domain.post.Post;

public interface CustomCommentRepository {
    List<Comment> findCommentsLatest(Post post, int count);

    List<Comment> findNestedCommentsById(Long commentId);
}
