package our.yurivongella.instagramclone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import our.yurivongella.instagramclone.controller.dto.member.ProfileDto;
import our.yurivongella.instagramclone.controller.dto.post.ProfilePostDto;
import our.yurivongella.instagramclone.controller.dto.member.SimpleProfileDto;
import our.yurivongella.instagramclone.domain.follow.FollowRepository;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.member.MemberRepository;
import our.yurivongella.instagramclone.domain.post.Post;
import our.yurivongella.instagramclone.domain.post.PostLike;
import our.yurivongella.instagramclone.domain.post.PostLikeRepository;
import our.yurivongella.instagramclone.domain.post.PostRepository;
import our.yurivongella.instagramclone.exception.CustomException;
import our.yurivongella.instagramclone.util.SecurityUtil;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static our.yurivongella.instagramclone.exception.ErrorCode.*;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProfileService {
    private static final PageRequest pageRequest = PageRequest.of(0, 12, Sort.by("id").descending());

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    public SimpleProfileDto getMySimpleProfile() {
        return SimpleProfileDto.of(getCurrentMember());
    }

    public ProfileDto getMyProfile() {
        return ProfileDto.of(getCurrentMember());
    }

    public ProfileDto getProfile(String displayId) {
        Member member = getMemberByDisplayId(displayId);
        ProfileDto profileDto = ProfileDto.of(member);

        followRepository.findByFromMemberAndToMember(getCurrentMember(), member)
                        .ifPresent(ignored -> profileDto.getMemberDto().setFollowTrue());

        return profileDto;
    }

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
                                .orElseThrow(() -> new CustomException(UNAUTHORIZED_MEMBER));
    }

    private Member getMemberByDisplayId(String displayId) {
        return memberRepository.findByDisplayId(displayId)
                                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
    }
}
