package our.yurivongella.instagramclone.domain.follow;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow,Long> {
    List<Follow> findByFromUserId(Long id);

    List<Follow> findByToUserId(Long id);

    Optional<Follow> findByFromUserIdAndToUserId(Long fromUserId, Long toUserId);
}
