package our.yurivongella.instagramclone.domain.dm.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document("direct_message")
@Getter
@Setter
public class DirectMessageDocument {

    @Id
    private String _id;
    private Map<Long, DMMeDocument> me;
}
