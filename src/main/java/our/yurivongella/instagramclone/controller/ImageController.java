package our.yurivongella.instagramclone.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import our.yurivongella.instagramclone.service.S3Service;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class ImageController {
    private final S3Service s3Service;

    @PostMapping("/images")
    public ResponseEntity<String> execWrite(MultipartFile file) throws IOException {
        String imgPath = s3Service.upload(file);
        return ResponseEntity.ok(imgPath);
    }
}
