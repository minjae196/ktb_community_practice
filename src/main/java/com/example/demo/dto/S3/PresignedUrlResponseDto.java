package com.example.demo.dto.S3;

import lombok.Builder;

@Builder
public record PresignedUrlResponseDto(
        String name,
        String url
){
}
