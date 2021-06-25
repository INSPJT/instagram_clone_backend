package our.yurivongella.instagramclone.controller.dto.comment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import our.yurivongella.instagramclone.entity.Comment;
import our.yurivongella.instagramclone.entity.Member;
import our.yurivongella.instagramclone.entity.Post;

import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

public class CommentDtoTest {

    @DisplayName("Comment -> CommentDto 테스트")
    @Test
    void test() {
        // given
        Member member = createMember();
        Post post = new Post(member, "test post", Collections.emptyList());
        Comment comment = new Comment(member, post, "test content");

        // when
        CommentDto commentDto = CommentDto.of(comment);

        // then
        assertThat(commentDto.getId()).isEqualTo(comment.getId());
        assertThat(commentDto.getContent()).isEqualTo("test content");
        assertThat(commentDto.getAuthor().getDisplayId()).isEqualTo("test");
        assertThat(commentDto.getIsLike()).isFalse();
        assertThat(commentDto.getLikeCount()).isEqualTo(0L);
        assertThat(commentDto.getNestedCommentCount()).isEqualTo(0);

        commentDto.setLikeTrue();
        assertThat(commentDto.getIsLike()).isTrue();
    }

    private Member createMember() {
        return Member.builder()
                     .displayId("test")
                     .nickname("test nickname")
                     .email("test@test.net")
                     .password("1q2w3e4r")
                     .profileImageUrl("test.img")
                     .build();
    }
}
