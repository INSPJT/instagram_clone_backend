package our.yurivongella.instagramclone.controller.dto.post;

import lombok.*;
import our.yurivongella.instagramclone.controller.dto.member.MemberResDto;
import our.yurivongella.instagramclone.entity.MediaUrl;
import our.yurivongella.instagramclone.entity.Post;
import our.yurivongella.instagramclone.util.DateTimeUtil;

import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String content;
    private MemberResDto author;

    private List<MediaUrlDto> mediaUrls;
    private Boolean isLike;

    private Long likeCount;
    private Long commentCount;
    private Long viewCount;

    private String createdDate;
    private String modifiedDate;

    public static PostDto of(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .author(MemberResDto.of(post.getMember()))
                .mediaUrls(post.getMediaUrls().stream().map(MediaUrlDto::of).collect(Collectors.toList()))
                .content(post.getContent())
                .isLike(false)
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .viewCount(post.getViews())
                .createdDate(DateTimeUtil.from(post.getCreatedDate()))
                .modifiedDate(DateTimeUtil.from(post.getModifiedDate()))
                .build();
    }

    public void setLikeTrue() {
        this.isLike = true;
    }
}
