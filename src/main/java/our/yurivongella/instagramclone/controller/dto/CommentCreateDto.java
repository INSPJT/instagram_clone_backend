package our.yurivongella.instagramclone.controller.dto;

import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentCreateDto {

    @NotNull
    private String content;

    public CommentCreateDto(String content) {
        this.content = content;
    }
}
