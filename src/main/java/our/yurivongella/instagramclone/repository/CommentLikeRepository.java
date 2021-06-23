package our.yurivongella.instagramclone.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import our.yurivongella.instagramclone.entity.Comment;
import our.yurivongella.instagramclone.entity.CommentLike;
import our.yurivongella.instagramclone.entity.Member;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByMemberAndComment(Member member, Comment comment);
}
