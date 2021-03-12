package our.yurivongella.instagramclone.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import our.yurivongella.instagramclone.controller.dto.FollowRequestDto;
import our.yurivongella.instagramclone.controller.dto.MemberResponseDto;
import our.yurivongella.instagramclone.domain.follow.Follow;
import our.yurivongella.instagramclone.domain.follow.FollowRepository;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.member.MemberRepository;
import our.yurivongella.instagramclone.util.SecurityUtil;

@Slf4j
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
        log.info(currentMember.getDisplayId() + "가 " + targetMember.getDisplayId() + "를 팔로우 합니다.");
        if (currentMember.equals(targetMember)) {
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
        ).orElseThrow(() -> new RuntimeException("팔로우 중이지 않습니다.")).unfollow();

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
                               .orElseThrow(() -> new UsernameNotFoundException("현재 계정 정보가 존재하지 않습니다."));
    }
}

