package our.yurivongella.instagramclone.service;

import static our.yurivongella.instagramclone.exception.ErrorCode.UNAUTHORIZED_MEMBER;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import our.yurivongella.instagramclone.controller.dto.comment.ProcessStatus;
import our.yurivongella.instagramclone.entity.Member;
import our.yurivongella.instagramclone.repository.MemberRepository;
import our.yurivongella.instagramclone.exception.CustomException;
import our.yurivongella.instagramclone.exception.ErrorCode;
import our.yurivongella.instagramclone.util.SecurityUtil;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public ProcessStatus deactivate() {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new CustomException(UNAUTHORIZED_MEMBER));
        if(!member.isActive()) throw new CustomException(ErrorCode.ALREADY_DEACTIVATED);
        member.deactivate();
        return ProcessStatus.SUCCESS;
    }

    @Transactional
    public ProcessStatus activate() {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new CustomException(UNAUTHORIZED_MEMBER));
        if(member.isActive()) throw new CustomException(ErrorCode.ALREADY_ACTIVATED);
        member.activate();
        return ProcessStatus.SUCCESS;
    }

    @Transactional
    protected void delete() {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new CustomException(UNAUTHORIZED_MEMBER));
        memberRepository.delete(member);
    }
}
