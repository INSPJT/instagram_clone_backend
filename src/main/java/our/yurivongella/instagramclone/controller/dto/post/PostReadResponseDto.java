package our.yurivongella.instagramclone.controller.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.post.PictureURL;
import our.yurivongella.instagramclone.domain.post.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PostReadResponseDto {
    Long id;
    MemberDto author;
    @JsonProperty("images")
    List<String> pictureUrls;
    @JsonProperty("body")
    String content;
    LocalDateTime created;
    Boolean isLike;
    @JsonProperty("likeUser")
    MemberDto usersWhoLike;
    @JsonProperty("likeLength")
    Long likeCount;
    List<CommentResponseDto> commentPreview;
    @JsonProperty("commentLength")
    Long commentCount;
    Long viewCount;
    //Boolean bookMark;

    @Builder
    public PostReadResponseDto(Long id, MemberDto author, List<String> pictureUrls, String content, LocalDateTime created, Boolean isLike, MemberDto usersWhoLike, Long likeCount, List<CommentResponseDto> commentPreview, Long commentCount, Long viewCount) {
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

    public static PostReadResponseDto toPostResponseDto(Post post, Member member) {
        return PostReadResponseDto.builder()
                .id(post.getId())
                .author(MemberDto.toMemberDto(post.getMember(), member))
                .pictureUrls(post.getPictureURLs().stream().map(PictureURL::getUrl).collect(Collectors.toList()))
                .content(post.getContent())
                .isLike(post.getPostLikes().stream().anyMatch(v -> v.getMember().getId().equals(member.getId())))
                .usersWhoLike(post.getPostLikes().size() == 0 ? null : post.getPostLikes().stream().findFirst().map(v -> MemberDto.toMemberDto(v.getMember(), member)).get())
                .likeCount((long) post.getPostLikes().size())
                .commentPreview(post.getComments().stream().limit(3).map(v -> CommentResponseDto.toCommentResponseDto(v, member)).collect(Collectors.toList()))
                .commentCount((long) post.getComments().size())
                .viewCount(post.getViews())
                .build();
    }
}