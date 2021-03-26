package our.yurivongella.instagramclone.controller.dto.socket.dm;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DirectMessageResponseDto {
    private String senderDisplayId;
    private String receiverDisplayId;
    private String message;
    private LocalDateTime createdTime;
}
