package our.yurivongella.instagramclone.domain.dm.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@Setter
public class DMMessage {

    @Id
    private String _id;
    private String body;
    private LocalDateTime createdAt;
}
