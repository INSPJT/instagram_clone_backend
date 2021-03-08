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
    private Long id;
    private MemberDto author;
    @JsonProperty("images")
    private List<String> pictureUrls;
    @JsonProperty("body")
    private String content;
    private LocalDateTime created;
    private Boolean isLike;
    @JsonProperty("likeUser")
    private MemberDto usersWhoLike;
    @JsonProperty("likeLength")
    private Long likeCount;
    private List<CommentResponseDto> commentPreview;
    @JsonProperty("commentLength")
    private Long commentCount;
    private Long viewCount;
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

    public static PostReadResponseDto of(Post post, Member member) {
        return PostReadResponseDto.builder()
                .id(post.getId())
                .author(MemberDto.of(post.getMember(), member))
                .pictureUrls(post.getPictureURLs().stream().map(PictureURL::getUrl).collect(Collectors.toList()))
                .content(post.getContent())
                .isLike(post.getPostLikes().stream().anyMatch(v -> v.getMember().getId().equals(member.getId())))
                .usersWhoLike(post.getPostLikes().isEmpty() ? null : post.getPostLikes().stream().findFirst().map(v -> MemberDto.of(v.getMember(), member)).get())
                .likeCount((long) post.getPostLikes().size())
                .commentPreview(post.getComments().stream().limit(3).map(v -> CommentResponseDto.of(v, member)).collect(Collectors.toList()))
                .commentCount((long) post.getComments().size())
                .viewCount(post.getViews())

                .build();
    }
}