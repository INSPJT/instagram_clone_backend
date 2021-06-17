package our.yurivongella.instagramclone.service;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import our.yurivongella.instagramclone.util.SecurityUtil;

@Slf4j
@Service
@NoArgsConstructor
public class S3Service {
    private AmazonS3 s3Client;

    @Value("${vault.url}")
    private URI url;

    @Value("${vault.token}")
    private String token;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void setS3Client() {
        log.info("[접속 경로] : {}", url);
        VaultTemplate vaultTemplate = new VaultTemplate(VaultEndpoint.from(url), new TokenAuthentication(token));
        VaultResponse vaultResponse = vaultTemplate.read("/aws/insta");
        Map<String, Object> data = vaultResponse.getData();

        String accessKey = data.get("accessKey").toString();
        String secretKey = data.get("secretKey").toString();

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        s3Client = AmazonS3ClientBuilder.standard()
                                        .withCredentials(new AWSStaticCredentialsProvider(credentials))
                                        .withRegion(this.region)
                                        .build();

    }

    public String upload(MultipartFile file) throws IOException {
        long now = (new Date()).getTime();
        String filenameWithTime = now + "-" + file.getOriginalFilename();
        String fileName = createFileName(FolderName.MEMBER, filenameWithTime);

        s3Client.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), null)
                                   .withCannedAcl(CannedAccessControlList.PublicRead)); // 외부에 공개할 이미지이므로, 해당 파일에 public read 권한을 추가
        return s3Client.getUrl(bucket, fileName).toString();
    }

    private String createFileName(FolderName type, String originalFilename) {
        return type.name() + "/" + SecurityUtil.getCurrentMemberId() + "/" + originalFilename;
    }

    enum FolderName {
        MEMBER
    }
}

