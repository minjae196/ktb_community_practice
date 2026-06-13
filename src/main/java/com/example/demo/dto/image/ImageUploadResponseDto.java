package com.example.demo.dto.image;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageUploadResponseDto {
    private String jpgUrl;
    private String webpUrl;
}
