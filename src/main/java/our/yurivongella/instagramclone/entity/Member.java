package our.yurivongella.instagramclone.entity;

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
import our.yurivongella.instagramclone.exception.CustomException;

import static our.yurivongella.instagramclone.exception.ErrorCode.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "member")
@ToString(of = { "displayId", "email" })
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

    @Nullable
    private String introduction;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "member_post_count")
    private Long postCount;

    @Column(name = "member_following_count")
    private Long followingCount;

    @Column(name = "member_follower_count")
    private Long followerCount;

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
        this.postCount = 0L;
        this.followingCount = 0L;
        this.followerCount = 0L;
        this.active = true;
    }

    public enum Authority {
        ROLE_USER, ROLE_ADMIN
    }

    @PrePersist
    public void prePersist() {
        this.active = true;
        this.postCount = 0L;
        this.followingCount = 0L;
        this.followerCount = 0L;
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public boolean equals(Member other) {
        return Objects.equals(id, other.getId());
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

    public void plusFollowingCount() {
        this.followingCount += 1;
    }

    public void minusFollowingCount() {
        if (this.followingCount <= 0) { throw new CustomException(INVALID_STATUS); }
        this.followingCount -= 1;
    }

    public void plusFollowerCount() {
        this.followerCount += 1;
    }

    public void minusFollowerCount() {
        if (this.followerCount <= 0) { throw new CustomException(INVALID_STATUS); }
        this.followerCount -= 1;
    }

    public void plusPostCount() {
        this.postCount += 1;
    }

    public void minusPostCount() {
        if (this.postCount <= 0) { throw new CustomException(INVALID_STATUS); }
        this.postCount -= 1;
    }

    public boolean isActive(){
        return this.active.booleanValue();
    }
}
