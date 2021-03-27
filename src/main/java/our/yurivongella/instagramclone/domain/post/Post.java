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

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
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

    @Column(name = "views", columnDefinition = "long default 0")
    private Long views;

    @Column(name = "post_like_count", columnDefinition = "long default 0")
    private Long likeCount;

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
    }

    public Post addMember(Member member) {
        this.member = member;
        member.getPosts().add(this);
        return this;
    }

    public void like() {
        this.likeCount += 1;
    }

    public void unlike() {
        if (this.likeCount <= 0) throw new CustomException(ErrorCode.INVALID_STATUS);
        this.likeCount -= 1;
    }

    public void viewCountUp() {
        this.views += 1;
    }
}
