package our.yurivongella.instagramclone.controller.dto.comment;

import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentCreateDto {

    private String content;

    public CommentCreateDto(String content) {
        this.content = content;
    }
}
