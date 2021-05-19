package our.yurivongella.instagramclone.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_DUP_CHK_REQUEST(BAD_REQUEST, "중복 체크를 원하는 대상은 반드시 1개여야 합니다"),
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰이 유효하지 않습니다"),
    MISMATCH_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰의 유저 정보가 일치하지 않습니다"),
    CANNOT_FOLLOW_MYSELF(BAD_REQUEST, "자기 자신은 팔로우 할 수 없습니다"),
    WEBSOCKET_BAD_SUBSCRIBE(BAD_REQUEST, "지원하지 않는 자원을 구독하셨습니다"),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "권한 정보가 없는 토큰입니다"),
    UNAUTHORIZED_MEMBER(UNAUTHORIZED, "현재 내 계정 정보가 존재하지 않습니다"),
    WEBSOCKET_NOT_LOGIN(UNAUTHORIZED, "로그인하지 않은 이용자는 웹소켓을 연결할 수 없습니다"),
    WEBSOCKET_UNAUTHORIZED(UNAUTHORIZED, "로그인된 유저는 다른 유저의 구독지점을 구독할 수 없습니다"),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    MEMBER_NOT_FOUND(NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
    REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "로그아웃 된 사용자입니다"),
    NOT_FOLLOW(NOT_FOUND, "팔로우 중이지 않습니다"),
    POST_NOT_FOUND(NOT_FOUND, "해당 포스트가 존재하지 않습니다."),
    ALREADY_UNLIKE(NOT_FOUND, "더 이상 좋아요를 취소할 수 없습니다"),
    COMMENT_NOT_FOUND(NOT_FOUND, "해당 댓글이 존재하지 않습니다."),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다"),
    ALREADY_LIKE(CONFLICT, "더 이상 좋아요를 할 수 없습니다"),

    /* 서버 내 데이터 오류 */
    INVALID_STATUS(INTERNAL_SERVER_ERROR, "서버 내 데이터에 오류가 있습니다");

    private final HttpStatus httpStatus;
    private final String detail;
}
