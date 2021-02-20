package our.yurivongella.instagramclone.domain.Follow;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow,Long> {
    List<Follow> findByFromUserId(Long id);

    List<Follow> findByToUserId(Long id);
}
