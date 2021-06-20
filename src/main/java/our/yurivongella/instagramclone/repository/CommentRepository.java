package our.yurivongella.instagramclone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import our.yurivongella.instagramclone.entity.Post;
import our.yurivongella.instagramclone.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findTop3ByPostOrderByIdDesc(Post post);
}
