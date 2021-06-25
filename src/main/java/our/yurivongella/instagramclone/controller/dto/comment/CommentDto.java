package our.yurivongella.instagramclone.controller.dto.comment;

import lombok.*;
import our.yurivongella.instagramclone.controller.dto.member.MemberResDto;
import our.yurivongella.instagramclone.entity.Comment;

import java.time.LocalDateTime;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private MemberResDto author;
    private Boolean isLike;
    private Long likeCount;
    private Integer nestedCommentCount;
    private LocalDateTime createdDate;

    public static CommentDto of(Comment comment) {
        return builder()
              .id(comment.getId())
              .content(comment.getContent())
              .author(MemberResDto.of(comment.getMember()))
              .isLike(false)
              .likeCount(comment.getLikeCount())
              .nestedCommentCount(comment.getNestedComments().size())
              .createdDate(comment.getCreatedDate())
              .build();
    }

    public void setLikeTrue() {
        this.isLike = true;
    }
}
