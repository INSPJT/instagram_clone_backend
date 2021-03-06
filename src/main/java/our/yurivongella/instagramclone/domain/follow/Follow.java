package our.yurivongella.instagramclone.domain.follow;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import our.yurivongella.instagramclone.domain.BaseEntity;
import our.yurivongella.instagramclone.domain.member.Member;

@Getter
@Entity
@NoArgsConstructor
@Table(
        name = "follow",
        uniqueConstraints = {@UniqueConstraint(columnNames = { "from_member_id", "to_member_id" })}
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

    @Deprecated
    @Builder
    public Follow(Member fromMember, Member toMember) {
        this.fromMember = fromMember;
        this.toMember = toMember;
    }

    public void addFollow(Member fromMember, Member toMember) {
        this.fromMember = fromMember;
        this.toMember = toMember;

        fromMember.getFollowers().add(this);
        toMember.getFollowings().add(this);
    }

    public void unFollow() {
        this.fromMember.getFollowings().remove(this);
        this.toMember.getFollowers().remove(this);
    }

}
