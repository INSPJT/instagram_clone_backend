package our.yurivongella.instagramclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class InstagramcloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(InstagramcloneApplication.class, args);
    }

}
