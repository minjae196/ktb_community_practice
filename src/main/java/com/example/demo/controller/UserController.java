package com.example.demo.controller;

import com.example.demo.config.filter.CustomUserDetails;
import com.example.demo.dto.user.SignupRequestDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.dto.user.UserUpdatedRequestDto;
import com.example.demo.service.UserService;
import com.example.demo.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> signUp(@RequestBody SignupRequestDto requestDto) {
        userService.SignUp(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<Void>of("SIGNUP_SUCCESS",null));
    }


    @PatchMapping("/me/info")
    public ResponseEntity<ApiResponse<Void>> updateMyInfo(@RequestBody UserUpdatedRequestDto updateDto,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails){

        userService.updateUserInfo(userDetails.getUserId(),updateDto);
        return ResponseEntity.ok(ApiResponse.<Void>of("INFO_UPDATED",null));
    }

    @PutMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(@RequestBody UserUpdatedRequestDto updateDto,
                                 @AuthenticationPrincipal CustomUserDetails userDetails){
        userService.updateUserPassword(userDetails.getUserId(),updateDto);
        return ResponseEntity.ok(ApiResponse.<Void>of("PASSWORD_UPDATED",null));
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails){
        userService.deleteUser(userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.<Void>of("DELETE_SUCCESS",null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUsers(){
        List<UserResponseDto> users = userService.getAllusers();

        return ResponseEntity.ok(ApiResponse.of("SUCCESS",users));
    }
}

