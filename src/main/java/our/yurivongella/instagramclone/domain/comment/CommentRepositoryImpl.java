package our.yurivongella.instagramclone.domain.comment;

import static our.yurivongella.instagramclone.domain.comment.QComment.comment;

import java.util.List;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findNestedCommentsById(final Long commentId, final Long lastId, final int pageSize) {
        return queryFactory.selectFrom(comment)
                           .where(
                                   isNestedComment(commentId),
                                   ltCommentId(lastId)
                           )
                           .orderBy(comment.id.desc())
                           .limit(pageSize + 1)
                           .fetch();
    }

    private BooleanExpression isNestedComment(final Long commentId) {
        return comment.baseComment.id.eq(commentId);
    }

    @Override
    public List<Comment> findCommentsFromPost(final Long postId, final Long lastId, final int pageSize) {
        return queryFactory.selectFrom(comment)
                           .where(
                                   isNativeComment(postId),
                                   ltCommentId(lastId)
                                  )
                           .orderBy(comment.id.desc())
                           .limit(pageSize + 1)
                           .fetch();
    }

    private BooleanExpression isNativeComment(final Long postId) {
        return comment.post.id.eq(postId).and(comment.baseComment.isNull());
    }

    private BooleanExpression ltCommentId(final Long lastId) {
        return lastId == null ? null : comment.id.lt(lastId);
    }
}
