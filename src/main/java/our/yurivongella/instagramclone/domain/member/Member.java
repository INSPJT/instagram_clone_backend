package our.yurivongella.instagramclone.domain.member;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import our.yurivongella.instagramclone.domain.BaseEntity;
import our.yurivongella.instagramclone.domain.comment.Comment;
import our.yurivongella.instagramclone.domain.comment.CommentLike;
import our.yurivongella.instagramclone.domain.follow.Follow;
import our.yurivongella.instagramclone.domain.post.Post;
import our.yurivongella.instagramclone.domain.post.PostLike;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;
    private String email;
    private String nickName;
    private String password;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<CommentLike> commentLikes = new ArrayList<>();

    @OneToMany(mappedBy = "toMember")
    private List<Follow> followings = new ArrayList<>();

    @OneToMany(mappedBy = "fromMember")
    private List<Follow> followers = new ArrayList<>();

    @Builder
    public Member(String name, String email, String nickName, String password) {
        this.name = name;
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.authority = Authority.ROLE_USER;
    }

    public Follow follow(Member targetMember) {
        Follow follow = Follow.builder()
                              .fromMember(this)
                              .toMember(targetMember)
                              .build();

        this.followings.add(follow);
        targetMember.followers.add(follow);
        return follow;
    }

    public void unFollow(Follow follow) {
        follow.getToMember().getFollowers().remove(follow);
        this.followings.remove(follow);
    }

}
