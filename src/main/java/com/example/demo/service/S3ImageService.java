package com.example.demo.service;


import com.example.demo.dto.image.ImageUploadResponseDto;
import com.example.demo.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class S3ImageService implements ImageService{

    private final S3Client s3Client;
    private final ImageProcessor imageProcessor;
    private final ImageRepository imageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Override
    public ImageUploadResponseDto uploadImage(MultipartFile file, String type){
        ImageProcessor.ProcessedFiles files = imageProcessor.processImage(file,type);

        String uuid = UUID.randomUUID().toString();
        String pngKey = "images/post/" + uuid + ".png";
        String webpKey = "images/post/" + uuid + ".webp";

        uploadFile(files.getPngFile(),pngKey,"image/png");
        uploadFile(files.getWebpFile(),webpKey,"image/webp");

        return new ImageUploadResponseDto(
                getPublicUrl(pngKey),
                getPublicUrl(webpKey)
        );
    }

    private void uploadFile(File file, String key, String contentType) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();

        s3Client.putObject(request, RequestBody.fromFile(file));
    }

    public void deleteFile(String key){
        try{
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3Client.deleteObject(request);
        } catch (S3Exception e){
            log.error("S3 파일 삭제 실패 (S3 오류). Object Key: {}, 원인: {}", key, e.awsErrorDetails().errorMessage());
        } catch (Exception e){
            log.error("S3 파일 삭제 중 알 수 없는 에러 발생. Object Key: {}", key, e);
        }
    }



    private String getPublicUrl(String key) {
        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + key;
    }
}
