package our.yurivongella.instagramclone.controller.dto.dm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import our.yurivongella.instagramclone.controller.dto.MemberResponseDto;
import our.yurivongella.instagramclone.domain.dm.DirectMessageMessageType;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
public class DirectMessageRoomResponseDto {

    private String lastMessageId;
    private DirectMessageMessageType lastMessageType;
    private MemberResponseDto senderInfo;
    private String content;
    private LocalDateTime lastMessageSentDate;
}