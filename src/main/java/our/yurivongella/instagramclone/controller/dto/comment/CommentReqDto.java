package our.yurivongella.instagramclone.controller.dto.comment;

import javax.validation.constraints.NotBlank;

import our.yurivongella.instagramclone.entity.Member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import our.yurivongella.instagramclone.entity.Comment;
import our.yurivongella.instagramclone.entity.Post;

@Getter
@Setter
@NoArgsConstructor
public class CommentReqDto {

    @NotBlank
    private String content;

    public CommentReqDto(String content) {
        this.content = content;
    }

    public Comment toEntity(Member member, Post post) {
        return new Comment(member, post, content);
    }
}
