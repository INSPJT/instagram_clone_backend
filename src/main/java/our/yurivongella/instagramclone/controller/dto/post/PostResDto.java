package our.yurivongella.instagramclone.controller.dto.post;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import our.yurivongella.instagramclone.controller.dto.post.PostReadResDto;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PostResDto {
    private Boolean hasNext;
    private List<PostReadResDto> feeds;
}
