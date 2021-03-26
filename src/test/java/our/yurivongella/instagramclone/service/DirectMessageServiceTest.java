package our.yurivongella.instagramclone.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import our.yurivongella.instagramclone.controller.dto.socket.dm.DirectMessageRequestDto;
import our.yurivongella.instagramclone.controller.dto.socket.dm.DirectMessageResponseDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DirectMessageServiceTest {

    @Autowired
    private DirectMessageService directMessageService;

    @DisplayName("게시글 생성")
    @Test
    public void createDirectMessage() {
        DirectMessageRequestDto directMessageRequestDto = DirectMessageRequestDto.builder()
                .senderDisplayId("tor012")
                .receiverDisplayId("woody")
                .message("hi")
                .build();
        DirectMessageResponseDto directMessageResponseDto = directMessageService.create(directMessageRequestDto);

        assertThat(directMessageRequestDto.getSenderDisplayId()).isEqualTo(directMessageResponseDto.getSenderDisplayId());
        assertThat(directMessageRequestDto.getReceiverDisplayId()).isEqualTo(directMessageResponseDto.getReceiverDisplayId());
        assertThat(directMessageRequestDto.getMessage()).isEqualTo(directMessageResponseDto.getMessage());
    }


}
