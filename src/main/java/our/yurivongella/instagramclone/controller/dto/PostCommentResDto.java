package our.yurivongella.instagramclone.controller.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import our.yurivongella.instagramclone.controller.dto.comment.CommentDto;

@ToString
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostCommentResDto {
    private Long size; // 댓글 + 대댓글 개수
    private List<CommentDto> comments; // 그냥 댓글(대댓글 X)
    private List<CommentDto> preview; // 나 + 팔로우 한 사람의 댓글이있다면 최대 3개 보여주는 용
}
