package our.yurivongella.instagramclone.controller.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@ToString
@Getter
public enum ProcessStatus {
    SUCCESS(HttpStatus.OK, "정상적으로 처리되었습니다."),
    FAIL(HttpStatus.BAD_REQUEST, "정상적으로 처리가 되지 않았습니다.");

    private HttpStatus status;
    private String message;
}
