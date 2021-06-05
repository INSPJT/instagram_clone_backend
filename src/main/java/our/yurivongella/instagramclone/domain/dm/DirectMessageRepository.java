package our.yurivongella.instagramclone.domain.dm;

import org.springframework.data.mongodb.repository.MongoRepository;
import our.yurivongella.instagramclone.domain.dm.document.DMMeDocument;
import our.yurivongella.instagramclone.domain.dm.document.DirectMessageDocument;

public interface DirectMessageRepository extends MongoRepository<DirectMessageDocument, String> {
    DMMeDocument findByMe(Long currentMemberId);
}
