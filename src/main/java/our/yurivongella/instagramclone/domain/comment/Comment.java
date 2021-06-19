package our.yurivongella.instagramclone.domain.comment;

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

import lombok.Getter;
import lombok.NoArgsConstructor;
import our.yurivongella.instagramclone.domain.BaseEntity;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.post.Post;
import our.yurivongella.instagramclone.exception.CustomException;
import our.yurivongella.instagramclone.exception.ErrorCode;

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

    @Column(name = "comment_like_count", columnDefinition = "bigint default 0")
    private Long likeCount;

    /**
     * 대댓글
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nested_comment_id")
    private Comment nestedComment;

    @OneToMany(mappedBy = "nestedComment")
    private List<Comment> nestedComments = new ArrayList<>();

    public Comment create(Member member, Post post, String content) {
        this.member = member;
        this.post = post;
        this.content = content;
        this.likeCount = 0L;
        member.getComments().add(this);
        post.getComments().add(this);
        post.plusCommentCount();
        return this;
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
    public void addComment(Comment baseComment, Comment comment){
        baseComment.getNestedComments().add(comment);
        this.nestedComment = comment;
    }
}
