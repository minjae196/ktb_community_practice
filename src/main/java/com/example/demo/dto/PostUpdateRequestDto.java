package com.example.demo.dto;

import lombok.Getter;

@Getter
public class PostUpdateRequestDto {
    private String title;
    private String body;
    private String postImageUrl;
}
