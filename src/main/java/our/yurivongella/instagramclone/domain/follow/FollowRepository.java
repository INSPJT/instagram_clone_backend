package our.yurivongella.instagramclone.domain.follow;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import our.yurivongella.instagramclone.domain.member.Member;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByToMember(Member member);
    Optional<Follow> findByFromMemberAndToMember(Member fromMember, Member toMember);
}
