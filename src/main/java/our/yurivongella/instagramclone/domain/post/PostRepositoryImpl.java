package our.yurivongella.instagramclone.domain.post;

import static our.yurivongella.instagramclone.domain.follow.QFollow.follow;
import static our.yurivongella.instagramclone.domain.post.QPost.post;

import java.util.List;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostRepositoryImpl implements CustomPostRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> findAllByJoinFollow(final Long memberId, final Long lastId, final int pageSize) {
        return queryFactory
                .selectFrom(post)
                .where(
                        post.member.id.in(findFeedableMemberList(memberId)),
                        ltPostId(lastId)
                )
                .orderBy(post.id.desc())
                .limit(pageSize + 1)
                .fetch();
    }

    private List<Long> findFeedableMemberList(final Long memberId) {
        final List<Long> followings = queryFactory.select(follow.toMember.id)
                                                  .from(follow)
                                                  .where(follow.fromMember.id.eq(memberId))
                                                  .fetch();

        followings.add(memberId); // 자기 자신도 추가
        return followings;
    }

    private BooleanExpression ltPostId(final Long lastId) {
        return lastId == null ? null : post.id.lt(lastId);
    }
}
