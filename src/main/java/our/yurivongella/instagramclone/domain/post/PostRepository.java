package our.yurivongella.instagramclone.domain.post;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByMemberId(Long memberId);

    @Query(value = "select p from Follow as f left join fetch Post as p"
                   + " on f.toMember.id = p.member.id "
                   + "and f.fromMember.id = :memberId",
            countQuery = "select count(p) from Post p ")
    Slice<Post> findFeedByUserId(@Param("memberId") Long memberId, Pageable pageable);
}