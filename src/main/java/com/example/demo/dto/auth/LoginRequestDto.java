package com.example.demo.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // Json을 객체로 변활할 때 기본 생성자가 필요하기 때문
public class LoginRequestDto {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
