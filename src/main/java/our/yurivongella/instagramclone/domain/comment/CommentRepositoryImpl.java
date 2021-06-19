package our.yurivongella.instagramclone.domain.comment;

import static our.yurivongella.instagramclone.domain.comment.QComment.comment;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import our.yurivongella.instagramclone.domain.post.Post;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory queryFactory;

    public List<Comment> findCommentsLatest(final Post post, final int count) {
        return queryFactory.selectFrom(comment)
                           .where(comment.post.eq(post))
                           .orderBy(comment.id.desc())
                           .limit(count)
                           .fetch();
    }

    @Override
    public List<Comment> findNestedCommentsById(final Long commentId) {
        return queryFactory.selectFrom(comment)
                           .where(comment.baseComment.id.eq(commentId))
                           .fetch();
    }
}
