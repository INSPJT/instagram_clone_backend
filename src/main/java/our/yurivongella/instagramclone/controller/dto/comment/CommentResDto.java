package our.yurivongella.instagramclone.controller.dto.comment;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentResDto {
    private Boolean hasNext;
    private List<CommentDto> comments = new ArrayList<>();

    public static CommentResDto of(boolean hasNext, List<CommentDto> comments) {
        return builder()
              .hasNext(hasNext)
              .comments(comments)
              .build();
    }
}
