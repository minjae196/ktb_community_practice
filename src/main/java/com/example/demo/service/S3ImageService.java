package com.example.demo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.example.demo.dto.image.ImageUploadResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class S3ImageService implements ImageService{

    private final AmazonS3 amazonS3;
    private final ImageProcessor imageProcessor;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public ImageUploadResponseDto uploadImage(MultipartFile file, String type){
        ImageProcessor.ProcessedFiles files = imageProcessor.processImage(file,type);

        String uuid = UUID.randomUUID().toString();
        String pngKey = "images/post/" + uuid + ".png";
        String webpKey = "images/post/" + uuid + ".webp";

        amazonS3.putObject(bucketName, pngKey, files.getPngFile());
        amazonS3.putObject(bucketName, webpKey, files.getWebpFile());

        return new ImageUploadResponseDto(
                amazonS3.getUrl(bucketName,pngKey).toString(),
                amazonS3.getUrl(bucketName,webpKey).toString()
        );
    }
}
