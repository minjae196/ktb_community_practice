package com.example.demo.controller;

import com.example.demo.response.ApiResponse;
import com.example.demo.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @org.springframework.web.bind.annotation.GetMapping("/presigned-url")
    public ResponseEntity<ApiResponse<Map<String, String>>> getPresignedUrl(
            @org.springframework.web.bind.annotation.RequestParam(value = "extension", defaultValue = ".mp4") String extension) {

        Map<String, String> result = videoService.getPresignedUrl(extension);

        return ResponseEntity.ok(ApiResponse.of("PRESIGNED_URL_CREATE_SUCCESS", result));
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadVideo(
            @RequestPart("file") MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("첨부된 영상 파일이 없습니다.");
        }

        Map<String, String> result = videoService.uploadVideo(file);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of("VIDEO_UPLOAD_SUCCESS", result));
    }
}
