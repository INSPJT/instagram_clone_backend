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
    Long id;
    MemberDto author;
    Boolean isLike;
    @JsonProperty("likeLength")
    Long likeCount;
    //Long replyLength;
    LocalDateTime created;

    @Builder
    public CommentResponseDto(Long id, MemberDto author, Boolean isLike, Long likeCount, LocalDateTime created) {
        this.id = id;
        this.author = author;
        this.isLike = isLike;
        this.likeCount = likeCount;
        this.created = created;
    }

    public static CommentResponseDto toCommentResponseDto(Comment comment, Member member) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .author(MemberDto.toMemberDto(comment.getMember(), member))
                .isLike(member.getCommentLikes().stream().anyMatch(v -> v.getComment().getId().equals(comment.getId())))
                .likeCount((long) comment.getCommentLikes().size())
                .created(comment.getCreatedDate())
                .build();
    }
}
