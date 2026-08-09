package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoService {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.video-bucket:podcast-raw-data-545122900064-ap-northeast-2-an}")
    private String videoBucketName;

    @Value("${cloud.aws.s3.output-bucket:podcast-media-origin-545122900064-ap-northeast-2-an}")
    private String outputBucketName;

    @Value("${cloud.aws.region.static:ap-northeast-2}")
    private String region;

    private static final Set<String> ALLOWED_VIDEO_EXTENSIONS = Set.of(
            ".mp4", ".mov", ".avi", ".mkv", ".webm"
    );

    public Map<String, String> uploadVideo(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase()
                : "";

        if (!ALLOWED_VIDEO_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("지원하지 않는 영상 형식입니다. (mp4, mov, avi, mkv, webm만 가능)");
        }

        String fileBaseName = UUID.randomUUID().toString();
        String key = "videos/" + fileBaseName + extension;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(videoBucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        // MediaConvert HLS Output 경로: videos/{fileBaseName}/{fileBaseName}.m3u8
        String hlsKey = "videos/" + fileBaseName + "/" + fileBaseName + ".m3u8";
        String url = "https://" + outputBucketName + ".s3." + region + ".amazonaws.com/" + hlsKey;

        log.info("영상 업로드 완료 (HLS): {}", url);

        return Map.of("videoUrl", url);
    }
}
