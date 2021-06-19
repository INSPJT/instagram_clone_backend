package our.yurivongella.instagramclone.domain.post;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import our.yurivongella.instagramclone.domain.follow.QFollow;
import our.yurivongella.instagramclone.domain.post.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import our.yurivongella.instagramclone.domain.SliceHelper;

@RequiredArgsConstructor
public class PostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Post> findAllByJoinFollow(final Long memberId, final Pageable pageable) {
        List<Post> content = queryFactory
                .selectFrom(QPost.post)
                .join(QFollow.follow).on(QPost.post.member.id.eq(QFollow.follow.toMember.id))
                .where(QFollow.follow.fromMember.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return SliceHelper.getSlice(content, pageable);
    }
}
