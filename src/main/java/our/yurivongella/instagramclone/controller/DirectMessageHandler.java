package our.yurivongella.instagramclone.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;
import our.yurivongella.instagramclone.controller.dto.socket.dm.DirectMessageRequestDto;
import our.yurivongella.instagramclone.controller.dto.socket.dm.DirectMessageResponseDto;
import our.yurivongella.instagramclone.service.DirectMessageService;

@RestController
@RequiredArgsConstructor
@MessageMapping("/socket")
public class DirectMessageHandler {

    final DirectMessageService directMessageService;

    @MessageMapping("/dm")
    public void requestDirectMessage(DirectMessageRequestDto directMessageRequestDto) {
        DirectMessageResponseDto directMessageResponseDto = directMessageService.create(directMessageRequestDto);
        directMessageService.send(directMessageResponseDto);
    }
}
