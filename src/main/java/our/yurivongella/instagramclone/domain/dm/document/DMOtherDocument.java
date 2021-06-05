package our.yurivongella.instagramclone.domain.dm.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

@Getter
@Setter
public class DMOtherDocument {

    @Id
    private Long _id;
    private DMMessage latest;
    private List<DMMessage> messages;
}
