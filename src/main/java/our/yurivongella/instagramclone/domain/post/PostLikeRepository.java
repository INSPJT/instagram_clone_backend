package our.yurivongella.instagramclone.domain.post;

import net.bytebuddy.asm.TypeReferenceAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostIdAndMemberId(Long memberId, Long postId);
}
