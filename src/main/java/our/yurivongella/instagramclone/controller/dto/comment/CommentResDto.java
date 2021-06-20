package our.yurivongella.instagramclone.controller.dto.comment;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import our.yurivongella.instagramclone.domain.SliceHelper;
import our.yurivongella.instagramclone.domain.comment.Comment;
import our.yurivongella.instagramclone.domain.member.Member;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentResDto {
    private Boolean hasNext;
    private List<CommentDto> commentResDtos = new ArrayList<>();

    public static CommentResDto create(final Member member, final List<Comment> content, final int COMMENT_PAGE_SIZE) {
        CommentResDto commentResDto = new CommentResDto();
        final boolean hasNext = SliceHelper.hasNext(content, COMMENT_PAGE_SIZE);
        commentResDto.setCommentResDtos(SliceHelper.getContents(content, hasNext, COMMENT_PAGE_SIZE)
                                                   .stream()
                                                   .map(comment -> CommentDto.of(comment, member))
                                                   .collect(toList()));
        commentResDto.setHasNext(hasNext);
        return commentResDto;
    }
}
