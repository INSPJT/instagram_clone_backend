package our.yurivongella.instagramclone.controller;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;
import our.yurivongella.instagramclone.controller.dto.dm.DirectMessageRoomResponseDto;
import our.yurivongella.instagramclone.exception.CustomException;
import our.yurivongella.instagramclone.service.DirectMessageService;

import java.util.List;

@RestController
@MessageMapping("/directmessage")
@AllArgsConstructor
public class DirectMessageHandler {

    private final DirectMessageService directMessageService;

    /*
    @MessageMapping("/directmessage")
    @SendTo("/ws/subscribe/directmessage/username")
    @Deprecated
    public String testEcho(String messages) {
        System.out.println("client's request is " + messages);
        return "server's return is " + messages;
    }
     */

    @MessageMapping("/initialize")
    public List<DirectMessageRoomResponseDto> initialize() {
        return directMessageService.initialize();
    }

    @MessageMapping("/")
    public void requestDirectMessage() {

    }
}
