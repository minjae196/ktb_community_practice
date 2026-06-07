package com.example.demo.dto;

import com.example.demo.Entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostRequestDto {

    @NotBlank
    private String title;

    private String body;

    private String postImageUrl;
    private User author;


}
