package our.yurivongella.instagramclone.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import our.yurivongella.instagramclone.controller.dto.MemberResponseDto;
import our.yurivongella.instagramclone.controller.dto.dm.DirectMessageRoomResponseDto;
import our.yurivongella.instagramclone.domain.dm.DirectMessageMessageType;
import our.yurivongella.instagramclone.domain.dm.DirectMessageRepository;
import our.yurivongella.instagramclone.domain.dm.document.DMMeDocument;
import our.yurivongella.instagramclone.domain.member.MemberRepository;
import our.yurivongella.instagramclone.util.SecurityUtil;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DirectMessageService {

    private final DirectMessageRepository directMessageRepository;
    private final MemberRepository memberRepository;

    public List<DirectMessageRoomResponseDto> initialize() {
        DMMeDocument me = directMessageRepository.findByMe(SecurityUtil.getCurrentMemberId());
        List<DirectMessageRoomResponseDto> list = new ArrayList<>();
        for(var entry : me.getOther().values()) {
            var latest = entry.getLatest();
            list.add(DirectMessageRoomResponseDto.builder()
                    .lastMessageId(latest.get_id())
                    .lastMessageType(DirectMessageMessageType.STRING)
                    .senderInfo(MemberResponseDto.of(memberRepository.findById(entry.get_id()).get()))
                    .content(latest.getBody())
                    .lastMessageSentDate(latest.getCreatedAt())
                    .build());
        }
        return list;
    }
}
