package our.yurivongella.instagramclone.controller.dto.profile;

import lombok.*;
import our.yurivongella.instagramclone.controller.dto.post.MediaUrlDto;
import our.yurivongella.instagramclone.entity.Post;
import our.yurivongella.instagramclone.util.DateTimeUtil;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class ProfilePostDto {
    private Long postId;
    private List<MediaUrlDto> mediaUrls;
    private String content;
    private Long likeCount;
    private Long commentCount;
    private String createdAt;
    private String modifiedAt;
    private Boolean isLike;

    @Builder
    public ProfilePostDto(Long postId, List<MediaUrlDto> mediaUrls, String content, Long likeCount, Long commentCount, String createdAt, String modifiedAt) {
        this.postId = postId;
        this.mediaUrls = mediaUrls;
        this.content = content;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.isLike = false;
    }

    public static ProfilePostDto of(Post post) {
        return ProfilePostDto.builder()
                .postId(post.getId())
                .mediaUrls(post.getMediaUrls().stream().map(MediaUrlDto::of).collect(Collectors.toList()))
                .content(post.getContent())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .createdAt(DateTimeUtil.from(post.getCreatedDate()))
                .modifiedAt(DateTimeUtil.from(post.getModifiedDate()))
                .build();
    }

    public void setLikeTrue() {
        this.isLike = true;
    }
}
