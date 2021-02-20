package our.yurivongella.instagramclone.domain.Users;

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

import com.sun.istack.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import our.yurivongella.instagramclone.domain.BaseEntity;
import our.yurivongella.instagramclone.domain.Comment.Comment;
import our.yurivongella.instagramclone.domain.Comment.CommentLike;
import our.yurivongella.instagramclone.domain.Follow.Follow;
import our.yurivongella.instagramclone.domain.Post.Post;
import our.yurivongella.instagramclone.domain.Post.PostLike;

@Getter
@Entity
@NoArgsConstructor
public class Users extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String name;
    private String email;
    private String nickName;
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<PostLike> postLikes= new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<CommentLike> commentLikes = new ArrayList<>();

    @OneToMany(mappedBy = "toUser")
    private List<Follow> followings = new ArrayList<>();

    @OneToMany(mappedBy = "fromUser")
    private List<Follow> followers = new ArrayList<>();

    @Builder
    public Users(String name, String email, String nickName, String password){
        this.name = name;
        this.email = email;
        this.nickName = nickName;
        this.password = password;
    }

}
