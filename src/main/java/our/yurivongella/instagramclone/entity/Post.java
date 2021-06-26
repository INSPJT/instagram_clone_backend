package our.yurivongella.instagramclone.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

import our.yurivongella.instagramclone.exception.CustomException;
import our.yurivongella.instagramclone.exception.ErrorCode;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private Long views = 0L;

    @Column(name = "post_like_count")
    private Long likeCount = 0L;

    @Column(name = "post_comment_count")
    private Long commentCount = 0L;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    List<MediaUrl> mediaUrls = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    List<Comment> comments = new ArrayList<>();

    public Post(Member member, String content, List<String> imageUrls) {
        this.content = content;
        addPost(member);
        addMediaUrls(imageUrls);
    }

    private void addPost(Member member) {
        this.member = member;
        member.getPosts().add(this);
        member.plusPostCount();
    }

    private void addMediaUrls(List<String> imageUrls) {
        this.mediaUrls = imageUrls.stream()
                                  .map(url -> new MediaUrl(url, this))
                                  .collect(Collectors.toList());
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
