package our.yurivongella.instagramclone.domain.post;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import our.yurivongella.instagramclone.domain.member.Member;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByMemberId(Long memberId);

    @Query(value = "SELECT p" +
                   " FROM Post p" +
                   " JOIN Follow f" +
                   " ON p.member.id = f.toMember.id" +
                   " WHERE f.fromMember.id = :memberId AND p.id < :lastPostId")
    List<Post> findAllByJoinFollow(@Param("memberId") Long memberId, @Param("lastPostId") Long lastPostId, Pageable pageable);

    List<Post> findByMemberAndIdLessThan(Member member, Long lastPostId, Pageable pageable);
}