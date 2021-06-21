package our.yurivongella.instagramclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import our.yurivongella.instagramclone.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Optional<Member> findByDisplayId(String displayId);
}
