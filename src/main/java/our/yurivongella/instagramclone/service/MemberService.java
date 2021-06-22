package our.yurivongella.instagramclone.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import our.yurivongella.instagramclone.controller.dto.ProcessStatus;
import our.yurivongella.instagramclone.controller.dto.member.MemberResDto;
import our.yurivongella.instagramclone.entity.Member;
import our.yurivongella.instagramclone.exception.CustomException;
import our.yurivongella.instagramclone.exception.ErrorCode;
import our.yurivongella.instagramclone.repository.FollowRepository;
import our.yurivongella.instagramclone.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

import our.yurivongella.instagramclone.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    public MemberResDto getMyProfile() {
        return MemberResDto.of(getCurrentMember());
    }

    public MemberResDto getProfile(String displayId) {
        Member member = getMemberByDisplayId(displayId);
        MemberResDto memberResDto = MemberResDto.of(member);

        followRepository.findByFromMemberAndToMember(getCurrentMember(), member)
                        .ifPresent(ignored -> memberResDto.setFollowTrue());

        return memberResDto;
    }

    @Transactional
    public ProcessStatus deactivate() {
        Member member = getCurrentMember();

        if (!member.isActive()) {
            throw new CustomException(ErrorCode.ALREADY_DEACTIVATED);
        }

        member.deactivate();
        return ProcessStatus.SUCCESS;
    }

    @Transactional
    public ProcessStatus activate() {
        Member member = getCurrentMember();

        if (member.isActive()) {
            throw new CustomException(ErrorCode.ALREADY_ACTIVATED);
        }

        member.activate();
        return ProcessStatus.SUCCESS;
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
