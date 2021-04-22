package our.yurivongella.instagramclone.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DirectMessageHandler {

    @MessageMapping("/directmessage")
    @SendTo("/ws/subscribe/directmessage/username")
    public String testEcho(String messages) {
        System.out.println("client's request is " + messages);
        return "server's return is " + messages;
    }
}
