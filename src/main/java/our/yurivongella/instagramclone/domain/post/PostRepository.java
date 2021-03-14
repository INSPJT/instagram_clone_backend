package our.yurivongella.instagramclone.domain.post;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByMemberId(Long memberId);

    @Query(value = "SELECT p "
                   + "FROM Post p "
                   + "WHERE p.id < :offset "
                   + "and p.member.id IN ( SELECT f.toMember.id FROM Follow f WHERE f.fromMember.id  = :memberId) "
                   + "order by p.createdDate desc ",
            countQuery = "select count(p) from Post p ")
    Slice<Post> findTop5ByUserId(@Param("memberId") Long memberId, @Param("offset") Long offset, Pageable pageable);
}