package our.yurivongella.instagramclone.domain.dm;

import org.springframework.data.mongodb.repository.MongoRepository;
import our.yurivongella.instagramclone.domain.dm.document.DMMeDocument;
import our.yurivongella.instagramclone.domain.dm.document.DMCollection;

public interface DirectMessageRepository extends MongoRepository<DMCollection, String> {

    DMMeDocument findByMe(Long currentMemberId);
}