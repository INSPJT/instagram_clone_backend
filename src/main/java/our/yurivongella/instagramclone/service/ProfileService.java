package our.yurivongella.instagramclone.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import our.yurivongella.instagramclone.controller.dto.profile.ProfileDto;
import our.yurivongella.instagramclone.controller.dto.profile.ProfilePostDto;
import our.yurivongella.instagramclone.domain.follow.FollowRepository;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.member.MemberRepository;
import our.yurivongella.instagramclone.domain.post.PostLikeRepository;
import our.yurivongella.instagramclone.domain.post.PostRepository;
import our.yurivongella.instagramclone.exception.CustomException;
import our.yurivongella.instagramclone.util.SecurityUtil;

import java.util.List;
import java.util.stream.Collectors;

import static our.yurivongella.instagramclone.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {
    private static final PageRequest pageRequest = PageRequest.of(0, 12, Sort.by("id").descending());

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional(readOnly = true)
    public ProfileDto getMyProfile() {
        return ProfileDto.of(getCurrentMember());
    }

    @Transactional(readOnly = true)
    public ProfileDto getProfile(String displayId) {
        Member member = getMemberByDisplayId(displayId);

        ProfileDto profileDto = ProfileDto.of(member);

        followRepository.findByFromMemberIdAndToMemberId(SecurityUtil.getCurrentMemberId(), member.getId())
                        .ifPresent(ignored -> profileDto.getMemberDto().setFollowTrue());

        return profileDto;
    }

    @Transactional(readOnly = true)
    public List<ProfilePostDto> getMyPosts(Long lastPostId) {
        if (lastPostId == null) {
            lastPostId = Long.MAX_VALUE;
        }

        return postRepository.findByMemberAndIdLessThan(getCurrentMember(), lastPostId, pageRequest)
                             .stream()
                             .map(ProfilePostDto::of)
                             .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProfilePostDto> getPosts(String displayId, Long lastPostId) {
        if (lastPostId == null) {
            lastPostId = Long.MAX_VALUE;
        }

        return postRepository.findByMemberAndIdLessThan(getMemberByDisplayId(displayId), lastPostId, pageRequest)
                .stream()
                .map(ProfilePostDto::of)
                .peek(profilePostDto ->
                        postLikeRepository.findByPostIdAndMemberId(profilePostDto.getPostId(), SecurityUtil.getCurrentMemberId())
                                          .ifPresent(ignored -> profilePostDto.setLikeTrue())
                )
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
