package com.example.demo.controller;

import com.example.demo.config.filter.CustomUserDetails;
import com.example.demo.dto.image.FileUploadRequestDTO;
import com.example.demo.dto.image.ImageUploadResponseDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping()
    public ResponseEntity<ApiResponse<ImageUploadResponseDto>> uploadImage(
            @ModelAttribute FileUploadRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ){

        if (!requestDTO.isFileSizeValid()) {
            throw new IllegalArgumentException("파일 크기가 너무 큽니다 (최대 10MB).");
        }

        ImageUploadResponseDto result = imageService.uploadImage(requestDTO.getFile(), "POST");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<ImageUploadResponseDto>of("IMAGE_UPLOAD_SUCCESS", result));

    }
}
