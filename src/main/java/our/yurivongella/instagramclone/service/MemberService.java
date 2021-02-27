package our.yurivongella.instagramclone.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import our.yurivongella.instagramclone.controller.dto.MemberRequestDto;
import our.yurivongella.instagramclone.domain.follow.Follow;
import our.yurivongella.instagramclone.domain.follow.FollowRepository;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.member.MemberRepository;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    public Long signUp(MemberRequestDto memberRequestDto) {
        Member member = memberRequestDto.toMember();
        return memberRepository.save(member).getId();
    }

    public boolean follow(Long fromMemberId, Long toMemberId) {
        Optional<Member> fromMember = memberRepository.findById(fromMemberId);
        Optional<Member> toMember = memberRepository.findById(toMemberId);

        Follow follow = fromMember.get().follow(toMember.get());
//
//        Follow follow = new Follow();
//        follow.addFollow(fromMember.get(), toMember.get());
        followRepository.save(follow); //쿼리나감

        return true;
    }

    public boolean unFollow(Long fromMemberId, Long toMemberId) {
        Optional<Member> fromMember = memberRepository.findById(fromMemberId);
        Optional<Follow> follow = followRepository.findByFromMemberIdAndToMemberId(fromMemberId, toMemberId);

        fromMember.get().unFollow(follow.get());

        // Follow follow = new Follow();
        // follow.addFollow(fromMember.get(), toMember.get());
        followRepository.delete(follow.get()); //쿼리나감

        return true;
    }

    public List<Follow> getFollowers(Long id) {
        return followRepository.findByToMemberId(id);
    }

    public List<Follow> getFollowing(Long id) {
        return followRepository.findByFromMemberId(id);
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }
}

