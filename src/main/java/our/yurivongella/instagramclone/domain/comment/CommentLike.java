package our.yurivongella.instagramclone.domain.comment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import our.yurivongella.instagramclone.domain.BaseEntity;
import our.yurivongella.instagramclone.domain.member.Member;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "comment_like")
@ToString(of = "comment")
public class CommentLike extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_like_id")
    private Long id;

    @JoinColumn(name = "comment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public CommentLike like(Member member, Comment comment) {
        this.member = member;
        this.comment = comment;
        comment.getCommentLikes().add(this);
        return this;
    }

    public void unlike() {
        comment.getCommentLikes().remove(this);
    }
}
