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
    private List<CommentDto> commentResDtos = new ArrayList<>();

    public static CommentResDto of(boolean hasNext, List<CommentDto> commentResDto) {
        return CommentResDto.builder()
                            .hasNext(hasNext)
                            .commentResDtos(commentResDto)
                            .build();
    }
}
