package our.yurivongella.instagramclone.domain.Post;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.sun.istack.NotNull;

import lombok.NoArgsConstructor;
import our.yurivongella.instagramclone.domain.BaseEntity;

@Entity
@NoArgsConstructor
public class PictureURL extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
