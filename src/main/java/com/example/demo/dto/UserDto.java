package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    private String userNickname;
    private String userId;
    private String userEmail;
    private String userPassword;
    private String userProfileImage;



}
