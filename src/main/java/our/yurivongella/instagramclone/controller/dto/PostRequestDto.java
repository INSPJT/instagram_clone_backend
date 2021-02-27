package our.yurivongella.instagramclone.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import our.yurivongella.instagramclone.domain.Post.PictureURL;
import our.yurivongella.instagramclone.domain.Post.Post;
import our.yurivongella.instagramclone.domain.Users.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class PostRequestDto {
    private List<String> pictureUrls;
    private String content;

    public Post toPosts(Users user) {
        return Post.builder()
                .user(user)
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
