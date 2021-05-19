package our.yurivongella.instagramclone.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;
import our.yurivongella.instagramclone.controller.dto.dm.DirectMessageRoomResponseDto;

import java.util.List;

@RestController
@MessageMapping("/directmessage")
public class DirectMessageHandler {

    //@MessageMapping("/directmessage")
    @SendTo("/ws/subscribe/directmessage/username")
    public String testEcho(String messages) {
        System.out.println("client's request is " + messages);
        return "server's return is " + messages;
    }

    @MessageMapping("/initialize")
    public List<DirectMessageRoomResponseDto> initialize() {
        return null;
    }

    @MessageMapping("/")
    public void requestDirectMessage() {
        
    }
}
