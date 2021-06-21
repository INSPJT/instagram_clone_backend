package our.yurivongella.instagramclone.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import our.yurivongella.instagramclone.exception.CustomException;
import our.yurivongella.instagramclone.exception.ErrorCode;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "comment")
@Getter
@Entity
@NoArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private Set<CommentLike> commentLikes = new HashSet<>();

    @Column(name = "comment_like_count")
    private Long likeCount = 0L;

    /**
     * 대댓글
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_comment_id")
    private Comment baseComment;

    @OneToMany(mappedBy = "baseComment")
    private List<Comment> nestedComments = new ArrayList<>();

    public Comment(final Member member, final Post post, final String content) {
        this.member = member;
        this.content = content;
        addComment(post);
    }

    private void addComment(final Post post) {
        this.post = post;
        post.getComments().add(this);
        post.plusCommentCount();
    }

    public void plusLikeCount() {
        this.likeCount += 1;
    }

    public void minusLikeCount() {
        if (this.likeCount <= 0) { throw new CustomException(ErrorCode.INVALID_STATUS); }
        this.likeCount -= 1;
    }

    /**
     * 대댓글 달기
     */
    public void addComment(Comment baseComment) {
        this.baseComment = baseComment;
        baseComment.getNestedComments().add(this);
    }
}
