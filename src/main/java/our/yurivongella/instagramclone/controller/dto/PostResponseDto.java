package our.yurivongella.instagramclone.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import our.yurivongella.instagramclone.domain.Comment.Comment;
import our.yurivongella.instagramclone.domain.Post.PictureURL;
import our.yurivongella.instagramclone.domain.Post.Post;
import our.yurivongella.instagramclone.domain.Post.PostLike;
import our.yurivongella.instagramclone.domain.Users.Users;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private Users user;
    private String content;
    List<PictureURL> pictureURLs;
    List<PostLike> postLikes;
    List<Comment> comments;

    @Builder
    public PostResponseDto(Long id, Users user, String content, List<PictureURL> pictureURLs, List<PostLike> postLikes, List<Comment> comments) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.pictureURLs = pictureURLs;
        this.postLikes = postLikes;
        this.comments = comments;
    }

    public static PostResponseDto toPostResponseDto(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .user(post.getUser())
                .content(post.getContent())
                .pictureURLs(post.getPictureURLs())
                .postLikes(post.getPostLikes())
                .comments(post.getComments())
                .build();
    }
}