package our.yurivongella.instagramclone.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import our.yurivongella.instagramclone.entity.CommentLike;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByCommentIdAndMemberId(Long commentId, Long memberId);
}
