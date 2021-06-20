package our.yurivongella.instagramclone.controller.dto.comment;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentResDto {
    private Boolean hasNext;
    private List<CommentDto> commentResDtos = new ArrayList<>();
}
