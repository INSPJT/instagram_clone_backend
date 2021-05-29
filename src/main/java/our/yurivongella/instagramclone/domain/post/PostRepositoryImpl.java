package our.yurivongella.instagramclone.domain.post;

import static our.yurivongella.instagramclone.domain.follow.QFollow.follow;
import static our.yurivongella.instagramclone.domain.post.QPost.post;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.support.PageableExecutionUtils;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import our.yurivongella.instagramclone.domain.SliceHelper;

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

        System.out.println("size:"+content.size());

        return SliceHelper.getSlice(content, pageable);
    }
}
