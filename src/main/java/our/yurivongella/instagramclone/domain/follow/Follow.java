package our.yurivongella.instagramclone.domain.follow;

import java.util.List;

import javax.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import our.yurivongella.instagramclone.domain.BaseEntity;
import our.yurivongella.instagramclone.domain.member.Member;

@Getter
@Entity
@NoArgsConstructor
@Table(
        name = "follow",
        uniqueConstraints = { @UniqueConstraint(columnNames = { "from_member_id", "to_member_id" }) }
)
public class Follow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id")
    private Member fromMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id")
    private Member toMember;

    @Builder
    public Follow(Member fromMember, Member toMember) {
        this.fromMember = fromMember;
        this.toMember = toMember;
        fromMember.plusFollowingCount();
        fromMember.getFollowings().add(this);
        toMember.plusFollowerCount();
        toMember.getFollowers().add(this);
    }

    public Follow unfollow() {
        this.fromMember.minusFollowingCount();
        this.fromMember.getFollowings().remove(this);
        this.toMember.minusFollowerCount();
        this.toMember.getFollowers().remove(this);
        return this;
    }

    @Override
    public String toString() {
        return "Follow{" +
               "fromMember.id=" + fromMember.getId() +
               ", toMember.id=" + toMember.getId() +
               '}';
    }
}
