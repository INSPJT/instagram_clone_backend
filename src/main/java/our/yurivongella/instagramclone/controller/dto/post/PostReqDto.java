package our.yurivongella.instagramclone.controller.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import our.yurivongella.instagramclone.entity.Member;
import our.yurivongella.instagramclone.entity.Post;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostReqDto {
    private String content;
    private List<String> imageUrls;

    public Post toEntity(Member member) {
        return new Post(member, content, imageUrls);
    }
}
