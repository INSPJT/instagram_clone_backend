package our.yurivongella.instagramclone.domain.Follow;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import our.yurivongella.instagramclone.domain.BaseEntity;
import our.yurivongella.instagramclone.domain.Users.Users;

@Getter
@Entity
@NoArgsConstructor
public class Follow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private Users fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private Users toUser;

    @Deprecated
    @Builder
    public Follow(Users fromUser, Users toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }

    public void addFollow(Users fromUser, Users toUsers){
        this.fromUser=fromUser;
        this.toUser=toUsers;

        fromUser.getFollowers().add(this);
        toUsers.getFollowings().add(this);
    }

    public void unFollow() {
        this.fromUser.getFollowings().remove(this);
        this.toUser.getFollowers().remove(this);
    }

}
