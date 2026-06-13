package com.example.demo.service;

import com.example.demo.dto.image.FileUploadRequestDTO;
import com.example.demo.dto.image.ImageUploadResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ImageUploadResponseDto uploadImage(MultipartFile file, String type);
}
