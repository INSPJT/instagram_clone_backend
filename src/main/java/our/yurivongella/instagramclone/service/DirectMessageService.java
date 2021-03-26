package our.yurivongella.instagramclone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import our.yurivongella.instagramclone.controller.dto.socket.dm.DirectMessageRequestDto;
import our.yurivongella.instagramclone.controller.dto.socket.dm.DirectMessageResponseDto;
import our.yurivongella.instagramclone.domain.directmessage.DirectMessageRepository;

@RequiredArgsConstructor
@Service
public class DirectMessageService {

    final private DirectMessageRepository directMessageRepository;
    final private SimpMessagingTemplate simpMessagingTemplate;

    public DirectMessageResponseDto create(DirectMessageRequestDto directMessageRequestDto) {
        return directMessageRepository.insert(directMessageRequestDto);
    }

    public void send(DirectMessageResponseDto directMessageResponseDto) {
        simpMessagingTemplate.convertAndSend(
                "/" + directMessageResponseDto.getReceiverDisplayId(),
                directMessageResponseDto
        );
    }
}
