package our.yurivongella.instagramclone.repository.comment;

import our.yurivongella.instagramclone.entity.Comment;

import java.util.List;

public interface CustomCommentRepository {
    List<Comment> findNestedCommentsById(Long commentId, Long lastId, int pageSize);

    List<Comment> findCommentsFromPost(Long postId, Long lastId, int pageSize);
}
