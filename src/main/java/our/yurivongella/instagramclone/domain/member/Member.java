package our.yurivongella.instagramclone.domain.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;

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
@ToString(of = {"displayId","email"})
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @NotNull
    @Column(unique = true)
    private String displayId;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @Nullable
    private String nickname;

    @Nullable
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Authority authority;

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<CommentLike> commentLikes = new ArrayList<>();

    @OneToMany(mappedBy = "fromMember")
    private List<Follow> followings = new ArrayList<>();

    @OneToMany(mappedBy = "toMember")
    private List<Follow> followers = new ArrayList<>();

    @Builder
    public Member(String displayId, String email, String nickname, String password, String profileImageUrl) {
        this.displayId = displayId;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.authority = Authority.ROLE_USER;
    }

    public enum Authority {
        ROLE_USER, ROLE_ADMIN
    }

    public boolean equals(Member other) {
        return Objects.equals(id, other.getId()) || Objects.equals(email, other.getEmail());
    }

    public boolean isFollowingTo(Member other) {
        return followings.stream()
                .map(Follow::getToMember)
                .anyMatch(toMember -> toMember.equals(other));
    }

    public boolean isFollowedBy(Member other) {
        return followers.stream()
                .map(Follow::getFromMember)
                .anyMatch(fromMember -> fromMember.equals(other));
    }

}
