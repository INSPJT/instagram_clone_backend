package our.yurivongella.instagramclone.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import our.yurivongella.instagramclone.controller.dto.MemberResponseDto;
import our.yurivongella.instagramclone.domain.follow.Follow;
import our.yurivongella.instagramclone.domain.follow.FollowRepository;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.member.MemberRepository;
import our.yurivongella.instagramclone.exception.CustomException;
import our.yurivongella.instagramclone.util.SecurityUtil;

import static our.yurivongella.instagramclone.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    @Transactional
    public boolean follow(Long memberId) {
        if (SecurityUtil.getCurrentMemberId().equals(memberId))  {
            throw new CustomException(CANNOT_FOLLOW_MYSELF);
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
        Member currentMember = getCurrentMember();
        log.info("현재 자신의 follower를 알고 싶은 유저 = {}", currentMember.getDisplayId());

        return currentMember.getFollowers().stream()
                            .map(f -> {
                                Member fromMember = f.getFromMember();
                                log.info("{}를 팔로우 하는 유저 = {}", currentMember.getDisplayId(), fromMember.getDisplayId());
                                return fromMember;
                            })
                            .map(MemberResponseDto::of)
                            .collect(Collectors.toList());
    }

    @Transactional
    public List<MemberResponseDto> getFollowings() {
        Member currentMember = getCurrentMember();
        log.info("현재 자신이 following 하고 있는 사람들을 알고 싶은 유저 = {}", currentMember.getDisplayId());
        return currentMember.getFollowings().stream()
                            .map(f -> {
                                Member toMember = f.getToMember();
                                log.info("{}가 {}를 팔로잉 하고 있습니다.", currentMember.getDisplayId(), toMember.getDisplayId());
                                return toMember;
                            })
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

