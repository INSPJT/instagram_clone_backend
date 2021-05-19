package our.yurivongella.instagramclone.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.member.MemberRepository;
import our.yurivongella.instagramclone.exception.CustomException;
import our.yurivongella.instagramclone.exception.ErrorCode;
import our.yurivongella.instagramclone.jwt.TokenProvider;
import our.yurivongella.instagramclone.util.SecurityUtil;

import static our.yurivongella.instagramclone.exception.ErrorCode.UNAUTHORIZED_MEMBER;

@RequiredArgsConstructor
@Configuration
public class StompHandshakeInterceptor implements ChannelInterceptor {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        switch (accessor.getCommand()) {
            case CONNECT:
                // at handshake time
                // at least logged in user?
                try {
                    tokenProvider.validateToken(accessor.getFirstNativeHeader("Authorization"));
                } catch (Exception e) {
                    throw new CustomException(ErrorCode.WEBSOCKET_NOT_LOGIN);
                }
                break;
            case SUBSCRIBE:
                // at subscribe time
                // check valid subscribe resources
                if (!isValidSubscribeResources(accessor.getDestination())) {
                    throw new CustomException(ErrorCode.WEBSOCKET_BAD_SUBSCRIBE);
                }
                // check valid subscribe end-point
                if (!isValidSubscribeEndPoint(accessor.getDestination())) {
                    throw new CustomException(ErrorCode.WEBSOCKET_UNAUTHORIZED);
                }
                break;
            default:
                // ?.?
                // it just check for what another cases
                // DISCONNECT
                System.out.println("unhandled case : " + accessor.getCommand());
        }
        return message;
    }

    // all subscribe pattern is
    // /ws/subscribe/{resources}/username
    private boolean isValidSubscribeResources(String destination) {
        var split = destination.split("/");
        // common pattern
        if (!split[0].equals("ws")) return false;
        if (!split[1].equals("subscribe")) return false;

        // check resources
        if (split[2].equals("directmessage")) return true;
        return false;
    }

    // check login user == endpoint
    private boolean isValidSubscribeEndPoint(String destination) {
        var split = destination.split("/");
        return split[3].equals(getCurrentMember().getDisplayId());
    }

    private Member getCurrentMember() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new CustomException(UNAUTHORIZED_MEMBER));
    }
}
