package our.yurivongella.instagramclone;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import our.yurivongella.instagramclone.service.S3Service;

@SpringBootTest
class InstagramcloneApplicationTests {

	@MockBean
	private S3Service s3Service;

	@Test
	void contextLoads() {
	}

}
