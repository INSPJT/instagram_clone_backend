package our.yurivongella.instagramclone.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import our.yurivongella.instagramclone.entity.Member;
import our.yurivongella.instagramclone.entity.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByToMember(Member member);
    Optional<Follow> findByFromMemberAndToMember(Member fromMember, Member toMember);
}
