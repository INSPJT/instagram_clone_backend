package our.yurivongella.instagramclone.controller.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.post.PictureURL;
import our.yurivongella.instagramclone.domain.post.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class PostRequestDto {
    private List<String> pictureUrls;
    private String content;

    public Post toPosts(Member member) {
        return Post.builder()
                .content(content)
                .build();
    }

    public List<PictureURL> toPictureURLs(Post post) {
        return pictureUrls.stream().map(url -> PictureURL.builder()
                .url(url)
                .post(post)
                .build()).collect(Collectors.toList());
    }
}
