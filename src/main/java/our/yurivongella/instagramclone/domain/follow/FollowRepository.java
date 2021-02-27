package our.yurivongella.instagramclone.domain.follow;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow,Long> {
    List<Follow> findByFromMemberId(Long id);

    List<Follow> findByToMemberId(Long id);

    Optional<Follow> findByFromMemberIdAndToMemberId(Long fromMemberId, Long toMemberId);
}
