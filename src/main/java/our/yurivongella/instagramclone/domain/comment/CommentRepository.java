package our.yurivongella.instagramclone.domain.comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import our.yurivongella.instagramclone.domain.post.Post;

public interface CommentRepository extends JpaRepository<Comment, Long> , CustomCommentRepository {
}
