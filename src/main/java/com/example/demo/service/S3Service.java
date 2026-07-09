package com.example.demo.service;

import com.example.demo.dto.S3.PresignedUrlResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    // 단일 파일 업로드 Presigned URL 생성
    public PresignedUrlResponseDto generatePresignedUploadUrl(String key) {
        String encodedKey = encodeFileName(key);
        return PresignedUrlResponseDto.builder()
                .name(encodedKey)
                .url(generatePresignedUrl(encodedKey))
                .build();
    }

    // 공통 Presigned URL 생성 로직
    private String generatePresignedUrl(String key) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(objectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
        return presignedRequest.url().toString();
    }

    // 파일 이름을 UTF-8로 인코딩
    private String encodeFileName(String fileName) {
        System.out.println(URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        return URLEncoder.encode(fileName, StandardCharsets.UTF_8);
    }

}
