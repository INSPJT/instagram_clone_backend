package our.yurivongella.instagramclone.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import our.yurivongella.instagramclone.controller.dto.MemberResponseDto;
import our.yurivongella.instagramclone.domain.follow.Follow;
import our.yurivongella.instagramclone.domain.follow.FollowRepository;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.member.MemberRepository;
import our.yurivongella.instagramclone.exception.CustomException;
import our.yurivongella.instagramclone.util.SecurityUtil;

import static our.yurivongella.instagramclone.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    @Transactional
    public boolean follow(Long memberId) {
        if (SecurityUtil.getCurrentMemberId().equals(memberId))  {
            throw new RuntimeException("자기 자신은 팔로우 할 수 없습니다.");
        }

        Member currentMember = getCurrentMember();
        Member targetMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        Follow follow = Follow.builder()
                .fromMember(currentMember)
                .toMember(targetMember)
                .build();

        followRepository.save(follow);
        return true;
    }

    @Transactional
    public boolean unFollow(Long memberId) {
        Follow follow = followRepository.findByFromMemberIdAndToMemberId(
                                SecurityUtil.getCurrentMemberId(),
                                memberId
                        )
                        .orElseThrow(() -> new CustomException(NOT_FOLLOW))
                        .unfollow();

        followRepository.delete(follow);
        return true;
    }

    @Transactional
    public List<MemberResponseDto> getFollowers() {
        return getCurrentMember().getFollowers().stream()
                .map(Follow::getFromMember)
                .map(MemberResponseDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<MemberResponseDto> getFollowings() {
        return getCurrentMember().getFollowings().stream()
                .map(Follow::getToMember)
                .map(MemberResponseDto::of)
                .collect(Collectors.toList());
    }

    /**
     * private
     */
    private Member getCurrentMember() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new CustomException(UNAUTHORIZED_MEMBER));
    }
}

