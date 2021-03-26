package our.yurivongella.instagramclone.domain.directmessage;

import org.springframework.stereotype.Repository;
import our.yurivongella.instagramclone.controller.dto.socket.dm.DirectMessageRequestDto;
import our.yurivongella.instagramclone.controller.dto.socket.dm.DirectMessageResponseDto;

@Repository
public interface DirectMessageRepository {
    DirectMessageResponseDto insert(DirectMessageRequestDto directMessageRequestDto);
}
