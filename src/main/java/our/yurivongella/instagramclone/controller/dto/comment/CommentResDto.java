package our.yurivongella.instagramclone.controller.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import our.yurivongella.instagramclone.controller.dto.member.MemberDto;
import our.yurivongella.instagramclone.domain.member.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import our.yurivongella.instagramclone.domain.comment.Comment;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResDto {

    private Long id;

    private String content;

    private MemberDto author;

    private Boolean isLike;

    @JsonProperty("likeLength")
    private Long likeCount;

    private Integer replyLength;

    private LocalDateTime created;

    @Builder
    public CommentResDto(final Long id, final String content, final MemberDto author, final Boolean isLike, final Long likeCount, final Integer replyLength,
                         final LocalDateTime created) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.isLike = isLike;
        this.likeCount = likeCount;
        this.replyLength = replyLength;
        this.created = created;
    }

    public static CommentResDto of(Comment comment, Member member) {
        return builder()
                .id(comment.getId())
                .content(comment.getContent())
                .author(MemberDto.of(comment.getMember(), member))
                .isLike(comment.getCommentLikes().stream().anyMatch(v -> v.getMember().getId().equals(member.getId())))
                .likeCount(comment.getLikeCount())
                .replyLength(comment.getNestedComments().size())
                .created(comment.getCreatedDate())
                .build();
    }
}
