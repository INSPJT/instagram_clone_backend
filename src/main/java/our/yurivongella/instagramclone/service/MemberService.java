package our.yurivongella.instagramclone.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        if (currentMember.getId().equals(followRequestDto.getId()))  {
            throw new RuntimeException("자기 자신은 팔로우 할 수 없습니다.");
        }

        Optional<Member> targetMember = memberRepository.findById(followRequestDto.getId());

        if (targetMember.isEmpty()) {
            throw new RuntimeException("팔로우 대상이 존재하지 않습니다.");
        }


        boolean isAlreadyFollow = followRepository.findByFromMemberIdAndToMemberId(
                currentMember.getId(),
                targetMember.get().getId()
        ).isPresent();

        if (isAlreadyFollow) {
            throw new RuntimeException("이미 팔로우 중 입니다.");
        }

        Follow newFollow = currentMember.follow(targetMember.get());
        followRepository.save(newFollow);
        return true;
    }

    @Transactional
    public boolean unFollow(FollowRequestDto followRequestDto) {
        Member currentMember = getCurrentMember();


        if (currentMember.getId().equals(followRequestDto.getId()))  {
            throw new RuntimeException("자기 자신은 언팔로우 할 수 없습니다.");
        }

        Optional<Member> targetMember = memberRepository.findById(followRequestDto.getId());

        if (targetMember.isEmpty()) {
            throw new RuntimeException("언팔로우 대상이 존재하지 않습니다.");
        }


        Optional<Follow> follow = followRepository.findByFromMemberIdAndToMemberId(
                currentMember.getId(),
                targetMember.get().getId()
        );

        if (follow.isEmpty()) {
            throw new RuntimeException("팔로우 중이지 않습니다.");
        }

        currentMember.unFollow(follow.get());
        followRepository.delete(follow.get());
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

