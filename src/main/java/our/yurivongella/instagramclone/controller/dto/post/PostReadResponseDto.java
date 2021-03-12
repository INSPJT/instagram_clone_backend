package our.yurivongella.instagramclone.controller.dto.post;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.post.MediaUrl;
import our.yurivongella.instagramclone.domain.post.Post;

@Getter
@NoArgsConstructor
public class PostReadResponseDto {
    private Long id;
    private MemberDto author;
    @JsonProperty("images")
    private List<String> mediaUrls;
    @JsonProperty("body")
    private String content;
    private String createdAt;
    private String modifiedAt;
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
    public PostReadResponseDto(Long id, MemberDto author, List<String> mediaUrls, String content, String createdAt, String modifiedAt, Boolean isLike,
                               MemberDto usersWhoLike, Long likeCount, List<CommentResponseDto> commentPreview, Long commentCount, Long viewCount) {
        this.id = id;
        this.author = author;
        this.mediaUrls = mediaUrls;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
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
                                  .mediaUrls(post.getMediaUrls().stream().map(MediaUrl::getUrl).collect(Collectors.toList()))
                                  .content(post.getContent())
                                  .isLike(post.getPostLikes().stream().anyMatch(v -> v.getMember().getId().equals(member.getId())))
                                  .usersWhoLike(post.getPostLikes().isEmpty() ? null : post.getPostLikes().stream().findFirst().map(v -> MemberDto.of(v.getMember(), member)).get())
                                  .likeCount((long) post.getPostLikes().size())
                                  .commentPreview(post.getComments().stream().limit(3).map(v -> CommentResponseDto.of(v, member)).collect(Collectors.toList()))
                                  .commentCount((long) post.getComments().size())
                                  .viewCount(post.getViews())
                                  .createdAt(from(post.getCreatedDate()))
                                  .modifiedAt(from(post.getModifiedDate()))
                                  .build();
    }

    private static String from(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}