package com.example.demo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {

    private Integer id;
    private String email;
    private String nickname;
    private String profileImageUrl;

}
