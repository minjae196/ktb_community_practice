package com.example.demo.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.example.demo.dto.S3.PresignedUrlResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    // 단일 파일 업로드 Presigned URL 생성
    public PresignedUrlResponseDto generatePresignedUploadUrl(String key) {
        String encodedKey = encodeFileName(key);
        return PresignedUrlResponseDto.builder()
                .name(encodedKey)
                .url(generatePresignedUrl(encodedKey, HttpMethod.PUT))
                .build();
    }

    // 공통 Presigned URL 생성 로직
    private String generatePresignedUrl(String key, HttpMethod method) {
        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + 1000 * 60 * 10); // 10분 후 만료

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, key)
                        .withMethod(method)
                        .withExpiration(expiration);


        URL presignedUrl = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return presignedUrl.toString();
    }

    // 파일 이름을 UTF-8로 인코딩
    private String encodeFileName(String fileName) {
        System.out.println(URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        return URLEncoder.encode(fileName, StandardCharsets.UTF_8);
    }

}
