package our.yurivongella.instagramclone.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import our.yurivongella.instagramclone.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostIdAndMemberId(Long postId, Long memberId);
    List<PostLike> findByMemberAndPostIn(Member member, List<Post> posts);
}
