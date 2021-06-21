package our.yurivongella.instagramclone.repository.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import our.yurivongella.instagramclone.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> , CustomCommentRepository {
}
