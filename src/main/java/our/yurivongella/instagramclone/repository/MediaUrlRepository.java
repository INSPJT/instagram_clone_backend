package our.yurivongella.instagramclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import our.yurivongella.instagramclone.entity.MediaUrl;

public interface MediaUrlRepository extends JpaRepository<MediaUrl, Long> {
}
