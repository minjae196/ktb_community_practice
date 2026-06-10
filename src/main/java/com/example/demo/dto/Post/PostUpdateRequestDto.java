package com.example.demo.dto.Post;

import lombok.Getter;

@Getter
public class PostUpdateRequestDto {
    private String title;
    private String body;
    private String postImageUrl;
}
