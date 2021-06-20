package our.yurivongella.instagramclone.domain.story;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import our.yurivongella.instagramclone.domain.BaseEntity;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.post.MediaUrl;
import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Story extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "story_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.EAGER)
    private MediaUrl mediaUrl;

    public Story addMember(Member member) {
        this.member = member;
        member.getStories().add(this);
        return this;
    }
}
