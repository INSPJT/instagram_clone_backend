package our.yurivongella.instagramclone.domain.Post;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sun.istack.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import our.yurivongella.instagramclone.domain.BaseEntity;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "picture_url")
public class PictureURL extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "picture_url_id")
    private Long id;

    @NotNull
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public PictureURL(String url) {
        this.url = url;
    }
}
