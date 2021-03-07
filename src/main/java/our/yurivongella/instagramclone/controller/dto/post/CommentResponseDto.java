package our.yurivongella.instagramclone.controller.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import our.yurivongella.instagramclone.domain.comment.Comment;
import our.yurivongella.instagramclone.domain.member.Member;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;
    private MemberDto author;
    private Boolean isLike;
    @JsonProperty("likeLength")
    private Long likeCount;
    //Long replyLength;
    private LocalDateTime created;

    @Builder
    public CommentResponseDto(Long id, MemberDto author, Boolean isLike, Long likeCount, LocalDateTime created) {
        this.id = id;
        this.author = author;
        this.isLike = isLike;
        this.likeCount = likeCount;
        this.created = created;
    }

    public static CommentResponseDto of(Comment comment, Member member) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .author(MemberDto.of(comment.getMember(), member))
                .isLike(member.getCommentLikes().stream().anyMatch(v -> v.getComment().getId().equals(comment.getId())))
                .likeCount((long) comment.getCommentLikes().size())
                .created(comment.getCreatedDate())
                .build();
    }
}