package our.yurivongella.instagramclone.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import our.yurivongella.instagramclone.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByMemberAndPost(Member member, Post post);
    List<PostLike> findByMemberAndPostIn(Member member, List<Post> posts);
}
