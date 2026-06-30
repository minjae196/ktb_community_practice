package com.example.demo.controller;


import com.example.demo.dto.S3.PresignedUrlResponseDto;
import com.example.demo.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @GetMapping("/presigned/upload")
    public PresignedUrlResponseDto getPresignedUploadUrl(@RequestParam String key) {
        return s3Service.generatePresignedUploadUrl(key);
    }

}
