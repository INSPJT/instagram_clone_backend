package our.yurivongella.instagramclone.domain.Post;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
}
