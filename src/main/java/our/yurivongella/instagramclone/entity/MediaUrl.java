package our.yurivongella.instagramclone.entity;

import javax.persistence.*;

import com.sun.istack.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "media_url")
public class MediaUrl extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_url_id")
    private Long id;

    @NotNull
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MediaUrlType mediaUrlType;

    @Transient
    private static List<String> extensions
            = List.of(
            "bmp",
            "gif",
            "jpeg",
            "jpg",
            "png",
            "svg+xml",
            "tiff",
            "webp",
            "x-icon"
    );

    @Builder
    public MediaUrl(String url, Post post) {
        this.url = url;
        this.post = post;
        this.mediaUrlType = extensions.stream().anyMatch(url::endsWith)
                            ? MediaUrlType.IMAGE
                            : MediaUrlType.VIDEO;
    }

    protected enum MediaUrlType {
        IMAGE, VIDEO
    }

    public String getTypeString() {
        return mediaUrlType.name();
    }
}
