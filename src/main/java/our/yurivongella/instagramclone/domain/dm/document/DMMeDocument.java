package our.yurivongella.instagramclone.domain.dm.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Map;

@Getter
@Setter
public class DMMeDocument {

    @Id
    private Long _id;
    private Map<Long, DMOtherDocument> other;
}
