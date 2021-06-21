package our.yurivongella.instagramclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import our.yurivongella.instagramclone.entity.Member;
import our.yurivongella.instagramclone.entity.Post;
import our.yurivongella.instagramclone.entity.PostLike;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByMemberAndPost(Member member, Post post);
    List<PostLike> findByMemberAndPostIn(Member member, List<Post> posts);
}
