package our.yurivongella.instagramclone.controller.dto.dm;

import lombok.Builder;
import our.yurivongella.instagramclone.controller.dto.MemberResponseDto;
import our.yurivongella.instagramclone.domain.dm.DirectMessageMessageType;

import java.time.LocalDateTime;

public class DirectMessageRoomResponseDto {
    private String lastMessageId;
    private DirectMessageMessageType lastMessageType;
    private MemberResponseDto senderInfo;
    private String content;
    private LocalDateTime lastMessageSentDate;

    @Builder
    public DirectMessageRoomResponseDto(String lastMessageId, DirectMessageMessageType lastMessageType, MemberResponseDto senderInfo, String content, LocalDateTime lastMessageSentDate) {
        this.lastMessageId = lastMessageId;
        this.lastMessageType = lastMessageType;
        this.senderInfo = senderInfo;
        this.content = content;
        this.lastMessageSentDate = lastMessageSentDate;
    }
}
