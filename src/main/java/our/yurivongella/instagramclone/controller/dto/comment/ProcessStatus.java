package our.yurivongella.instagramclone.controller.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProcessStatus {
    SUCCESS("정상적으로 처리되었습니다."),
    FAIL("정상적으로 처리가 되지 않았습니다.")
    ;

    private String message;
}
