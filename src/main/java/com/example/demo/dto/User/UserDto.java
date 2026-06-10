package com.example.demo.dto.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    private String userNickname;
    private Integer userId;
    private String userEmail;
    private String userPassword;
    private String userProfileImage;



}
