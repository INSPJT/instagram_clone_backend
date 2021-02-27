package our.yurivongella.instagramclone.domain.Post;

import java.util.ArrayList;
import java.util.List;

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
import our.yurivongella.instagramclone.domain.BaseEntity;
import our.yurivongella.instagramclone.domain.Comment.Comment;
import our.yurivongella.instagramclone.domain.Users.Users;

@Getter
@NoArgsConstructor
@Entity
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(columnDefinition = "text")
    private String content;

    private Long views;

    @OneToMany(mappedBy = "post")
    List<PictureURL> pictureURLs = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(Users user, String content) {
        this.user = user;
        user.getPosts().add(this);
        this.content = content;
        this.views = 0L;
    }
}
