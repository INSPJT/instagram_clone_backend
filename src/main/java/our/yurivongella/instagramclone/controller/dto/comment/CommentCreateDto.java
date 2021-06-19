package our.yurivongella.instagramclone.controller.dto.comment;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import our.yurivongella.instagramclone.domain.comment.Comment;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.post.Post;

@Getter
@Setter
@NoArgsConstructor
public class CommentCreateDto {

    @NotBlank
    private String content;

    public CommentCreateDto(String content) {
        this.content = content;
    }

    public Comment toEntity(Member member, Post post) {
        return new Comment(member, post, content);
    }
}
