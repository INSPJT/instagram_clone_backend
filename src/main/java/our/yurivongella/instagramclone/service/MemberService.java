package our.yurivongella.instagramclone.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import our.yurivongella.instagramclone.controller.dto.FollowRequestDto;
import our.yurivongella.instagramclone.controller.dto.MemberResponseDto;
import our.yurivongella.instagramclone.domain.follow.Follow;
import our.yurivongella.instagramclone.domain.follow.FollowRepository;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.member.MemberRepository;
import our.yurivongella.instagramclone.util.SecurityUtil;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    @Transactional
    public boolean follow(FollowRequestDto followRequestDto) {
        Member currentMember = getCurrentMember();
        Member targetMember = memberRepository.findById(followRequestDto.getId())
                .orElseThrow(() -> new RuntimeException("팔로우 대상이 존재하지 않습니다."));

        if (currentMember.equals(targetMember))  {
            throw new RuntimeException("자기 자신은 팔로우 할 수 없습니다.");
        }

        try {
            Follow follow = Follow.builder()
                    .fromMember(currentMember)
                    .toMember(targetMember)
                    .build();

            followRepository.save(follow);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("이미 팔로우 중 입니다.");
        }
    }

    @Transactional
    public boolean unFollow(FollowRequestDto followRequestDto) {
        Follow follow = followRepository.findByFromMemberIdAndToMemberId(
                                SecurityUtil.getCurrentMemberId(),
                                followRequestDto.getId()
                        )
                        .orElseThrow(() -> new RuntimeException("팔로우 중이지 않습니다."))
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
                .orElseThrow(() -> new UsernameNotFoundException("현재 계정 정보가 존재하지 않습니다."));
    }
}

