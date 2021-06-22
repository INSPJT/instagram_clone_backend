package our.yurivongella.instagramclone.service;

import lombok.RequiredArgsConstructor;
import our.yurivongella.instagramclone.controller.dto.profile.ProfilePostDto;
import our.yurivongella.instagramclone.entity.Member;
import our.yurivongella.instagramclone.entity.Post;
import our.yurivongella.instagramclone.entity.PostLike;
import our.yurivongella.instagramclone.repository.PostLikeRepository;
import our.yurivongella.instagramclone.repository.post.PostRepository;
import our.yurivongella.instagramclone.exception.CustomException;
import our.yurivongella.instagramclone.exception.ErrorCode;
import our.yurivongella.instagramclone.util.SecurityUtil;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import our.yurivongella.instagramclone.repository.MemberRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProfileService {
    private static final PageRequest pageRequest = PageRequest.of(0, 12, Sort.by("id").descending());

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    public List<ProfilePostDto> getMyPosts(Long lastPostId) {
        if (lastPostId == null) {
            lastPostId = Long.MAX_VALUE;
        }

        return postRepository.findByMemberAndIdLessThan(getCurrentMember(), lastPostId, pageRequest)
                             .stream()
                             .map(ProfilePostDto::of)
                             .collect(Collectors.toList());
    }

    public List<ProfilePostDto> getPosts(String displayId, Long lastPostId) {
        if (lastPostId == null) {
            lastPostId = Long.MAX_VALUE;
        }

        List<Post> posts = postRepository.findByMemberAndIdLessThan(getMemberByDisplayId(displayId), lastPostId, pageRequest);
        Set<Long> likedPostIds = postLikeRepository.findByMemberAndPostIn(getCurrentMember(), posts)
                                                   .stream()
                                                   .map(PostLike::getPost)
                                                   .map(Post::getId)
                                                   .collect(Collectors.toSet());

        return posts.stream()
                    .map(ProfilePostDto::of)
                    .peek(dto -> {
                        if (likedPostIds.contains(dto.getPostId())) {
                            dto.setLikeTrue();
                        }
                    })
                    .collect(Collectors.toList());
    }

    /**
     * private
     */
    private Member getCurrentMember() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_MEMBER));
    }

    private Member getMemberByDisplayId(String displayId) {
        return memberRepository.findByDisplayId(displayId)
                                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
