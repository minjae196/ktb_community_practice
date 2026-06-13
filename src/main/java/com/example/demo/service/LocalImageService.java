package com.example.demo.service;

import com.example.demo.dto.image.ImageUploadResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocalImageService implements ImageService{

    private final ImageProcessor imageProcessor;

    @Value("${file.dir}")
    private String fileDir;

    @Override
    public ImageUploadResponseDto uploadImage(MultipartFile file, String type){

        ImageProcessor.ProcessedFiles processedFiles =
                imageProcessor.processImage(file,type);

        // 파일 이름이 겹치지 않도록 고유한 UUID 생성
        String uuid = UUID.randomUUID().toString();
        String jpgFileName = uuid + ".jpg";
        String webpFileName = uuid + ".webp";

        File directory = new File(fileDir);
        if(!directory.exists()){
            directory.mkdirs();
        }

        File targetJpg = new File(fileDir + jpgFileName);
        File targetWebp = new File(fileDir + webpFileName);

        try{
            Files.copy(processedFiles.getJpgFile().toPath(), targetJpg.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(processedFiles.getWebpFile().toPath(), targetWebp.toPath(), StandardCopyOption.REPLACE_EXISTING);

            processedFiles.getJpgFile().delete();
            processedFiles.getWebpFile().delete();
        } catch(IOException e) {
            throw new RuntimeException("로컬 디스크에 파일 저장 중 오류가 발생했습니다.", e);
        }

        return new ImageUploadResponseDto("/images/" + jpgFileName, "/images/" + webpFileName);
    }

}
