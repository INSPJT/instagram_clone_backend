package our.yurivongella.instagramclone.controller.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import our.yurivongella.instagramclone.entity.MediaUrl;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaUrlDto {
    private Long mediaUrlId;
    private String url;
    private String type;

    public static MediaUrlDto of(MediaUrl mediaUrl) {
        return MediaUrlDto.builder()
                .mediaUrlId(mediaUrl.getId())
                .url(mediaUrl.getUrl())
                .type(mediaUrl.getTypeString())
                .build();
    }
}
