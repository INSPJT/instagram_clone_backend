package our.yurivongella.instagramclone.domain.comment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.member.MemberRepository;
import our.yurivongella.instagramclone.domain.post.Post;
import our.yurivongella.instagramclone.domain.post.PostRepository;
import our.yurivongella.instagramclone.service.S3Service;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentRepositoryTest {
    @MockBean
    private S3Service s3Service;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @DisplayName("DB 에서 특정 게시글에 달린 가장 최신 댓글 3 개 가져오기")
    @Test
    void findTop3ByPostOrderByIdDesc() {
        Member member = Member.builder().build();
        memberRepository.save(member);
        Post post = Post.builder().build().addMember(member);
        postRepository.save(post);

        IntStream.range(1, 11).forEach(i -> {
            Comment comment = new Comment(member, post, "comment content " + i);
            commentRepository.save(comment);
        });

        // when
        List<Comment> comments = commentRepository.findCommentsLatest(post, 3);

        // then
        assertThat(comments.size()).isEqualTo(3);
        comments.forEach(c -> assertThat(c.getPost().getId()).isEqualTo(post.getId()));
    }
}
