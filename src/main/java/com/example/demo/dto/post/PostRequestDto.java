package com.example.demo.dto.post;

import com.example.demo.Entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequestDto {

    @NotBlank
    @Size(min = 2, max = 26, message = "제목은 26자 이하로 입력해주세요.")
    private String title;

    private String body;

    private String postImageUrl;

    private String postVideoUrl;


}
