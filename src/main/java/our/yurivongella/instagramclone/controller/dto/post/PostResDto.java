package our.yurivongella.instagramclone.controller.dto.post;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResDto {
    private Boolean hasNext;
    private List<PostDto> posts = new ArrayList<>();

    public static PostResDto of(boolean hasNext, List<PostDto> posts) {
        return PostResDto.builder()
                         .hasNext(hasNext)
                         .posts(posts)
                         .build();
    }
}
