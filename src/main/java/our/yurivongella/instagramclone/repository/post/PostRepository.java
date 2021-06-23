package our.yurivongella.instagramclone.repository.post;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import our.yurivongella.instagramclone.entity.Member;
import our.yurivongella.instagramclone.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {
    List<Post> findAllByMember(Member member);
    List<Post> findByMemberAndIdLessThan(Member member, Long lastPostId, Pageable pageable);
}