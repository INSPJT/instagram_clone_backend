package our.yurivongella.instagramclone.controller.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import our.yurivongella.instagramclone.controller.dto.member.MemberDto;
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
    private String content;

    @Builder
    public CommentResponseDto(Long id, MemberDto author, Boolean isLike, Long likeCount, LocalDateTime created, String content) {
        this.id = id;
        this.author = author;
        this.isLike = isLike;
        this.likeCount = likeCount;
        this.created = created;
        this.content = content;
    }

    public static CommentResponseDto of(Comment comment, Member member) {
        return builder()
                .id(comment.getId())
                .author(MemberDto.of(comment.getMember(), member))
                .isLike(member.getCommentLikes().stream().anyMatch(v -> v.getComment().getId().equals(comment.getId())))
                .likeCount(comment.getLikeCount())
                .created(comment.getCreatedDate())
                .content(comment.getContent())
                .build();
    }
}
