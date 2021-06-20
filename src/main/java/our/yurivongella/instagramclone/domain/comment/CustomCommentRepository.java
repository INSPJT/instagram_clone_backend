package our.yurivongella.instagramclone.domain.comment;

import java.util.List;

public interface CustomCommentRepository {
    List<Comment> findNestedCommentsById(Long commentId, Long lastId, int pageSize);

    List<Comment> findCommentsFromPost(Long postId, Long lastId, int pageSize);
}
