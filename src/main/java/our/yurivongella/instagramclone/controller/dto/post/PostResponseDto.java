package our.yurivongella.instagramclone.controller.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import our.yurivongella.instagramclone.domain.Post.PictureURL;
import our.yurivongella.instagramclone.domain.Post.Post;
import our.yurivongella.instagramclone.domain.Users.Users;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    Long id;
    UsersDto author;
    @JsonProperty("images")
    List<String> pictureUrls;
    @JsonProperty("body")
    String content;
    LocalDateTime created;
    Boolean isLike;
    @JsonProperty("likeUser")
    UsersDto usersWhoLike;
    @JsonProperty("likeLength")
    Long likeCount;
    List<CommentResponseDto> commentPreview;
    @JsonProperty("commentLength")
    Long commentCount;
    Long viewCount;
    //Boolean bookMark;

    @Builder
    public PostResponseDto(Long id, UsersDto author, List<String> pictureUrls, String content, LocalDateTime created, Boolean isLike, UsersDto usersWhoLike, Long likeCount, List<CommentResponseDto> commentPreview, Long commentCount, Long viewCount) {
        this.id = id;
        this.author = author;
        this.pictureUrls = pictureUrls;
        this.content = content;
        this.created = created;
        this.isLike = isLike;
        this.usersWhoLike = usersWhoLike;
        this.likeCount = likeCount;
        this.commentPreview = commentPreview;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
    }

    public static PostResponseDto toPostResponseDto(Post post, Users user) {
        return PostResponseDto.builder()
                .id(post.getId())
                .author(UsersDto.toUsersDto(post.getUser(), user))
                .pictureUrls(post.getPictureURLs().stream().map(PictureURL::getUrl).collect(Collectors.toList()))
                .content(post.getContent())
                .isLike(post.getPostLikes().stream().anyMatch(v -> v.getUser().getId().equals(user.getId())))
                .usersWhoLike(post.getPostLikes().size() == 0 ? null : post.getPostLikes().stream().findFirst().map(v -> UsersDto.toUsersDto(v.getUser(), user)).get())
                .likeCount((long) post.getPostLikes().size())
                .commentPreview(post.getComments().stream().limit(3).map(v -> CommentResponseDto.toCommentResponseDto(v, user)).collect(Collectors.toList()))
                .commentCount((long) post.getComments().size())
                .viewCount(post.getViews())
                .build();
    }
}