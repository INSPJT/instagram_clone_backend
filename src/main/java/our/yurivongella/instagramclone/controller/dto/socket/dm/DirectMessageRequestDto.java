package our.yurivongella.instagramclone.controller.dto.socket.dm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DirectMessageRequestDto {
    String senderDisplayId;
    String receiverDisplayId;
    String message;
}