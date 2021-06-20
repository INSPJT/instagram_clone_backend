package our.yurivongella.instagramclone.controller.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.post.MediaUrl;
import our.yurivongella.instagramclone.domain.post.Post;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostCreateReqDto {
    private List<String> mediaUrls;
    private String content;

    public Post toPost(Member member) {
        return Post.builder()
                   .content(content)
                   .build()
                   .addMember(member);
    }

    public List<MediaUrl> getMediaUrls(Post post) {
        return mediaUrls.stream().map(url -> MediaUrl.builder()
                                                     .url(url)
                                                     .post(post)
                                                     .build()).collect(Collectors.toList());
    }
}
