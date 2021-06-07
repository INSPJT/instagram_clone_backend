package our.yurivongella.instagramclone.domain.post;

import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.PrePersist;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import our.yurivongella.instagramclone.domain.BaseEntity;
import our.yurivongella.instagramclone.domain.comment.Comment;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.exception.CustomException;
import our.yurivongella.instagramclone.exception.ErrorCode;

@Getter
@NoArgsConstructor
@Entity
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(columnDefinition = "text")
    private String content;

    @Column(name = "views")
    private Long views;

    @Column(name = "post_like_count")
    private Long likeCount;

    @Column(name = "post_comment_count")
    private Long commentCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    List<MediaUrl> mediaUrls = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(String content) {
        this.content = content;
        this.likeCount = 0L;
        this.views = 0L;
        this.commentCount = 0L;
    }

    @PrePersist
    public void prePersist() {
        this.views = 0L;
        this.likeCount = 0L;
        this.commentCount = 0L;
    }

    public Post addMember(Member member) {
        this.member = member;
        member.getPosts().add(this);
        member.plusPostCount();
        return this;
    }

    public void plusLikeCount() {
        this.likeCount += 1;
    }

    public void minusLikeCount() {
        if (this.likeCount <= 0) { throw new CustomException(ErrorCode.INVALID_STATUS); }
        this.likeCount -= 1;
    }

    public void plusCommentCount() {
        this.commentCount += 1;
    }

    public void minusCommentCount() {
        if (this.commentCount <= 0) { throw new CustomException(ErrorCode.INVALID_STATUS); }
        this.commentCount -= 1;
    }

    public void viewCountUp() {
        this.views += 1;
    }
}
