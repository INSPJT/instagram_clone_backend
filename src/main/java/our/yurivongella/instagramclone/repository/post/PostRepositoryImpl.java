package our.yurivongella.instagramclone.repository.post;

import static our.yurivongella.instagramclone.entity.QPost.post;
import static our.yurivongella.instagramclone.entity.QFollow.follow;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import our.yurivongella.instagramclone.util.SliceHelper;
import our.yurivongella.instagramclone.entity.Post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Post> findAllByJoinFollow(final Long memberId, final Pageable pageable) {
        List<Post> content = queryFactory
                .selectFrom(post)
                .join(follow).on(post.member.id.eq(follow.toMember.id))
                .where(follow.fromMember.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return SliceHelper.getSlice(content, pageable);
    }
}
