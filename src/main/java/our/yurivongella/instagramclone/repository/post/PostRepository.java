package our.yurivongella.instagramclone.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;

import our.yurivongella.instagramclone.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {
}
