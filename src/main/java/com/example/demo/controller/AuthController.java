package com.example.demo.controller;

import com.example.demo.service.AuthService;
import com.example.demo.dto.auth.LoginRequestDto;
import com.example.demo.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ApiResponse<Void> login(@RequestBody LoginRequestDto loginDto,
                             HttpServletRequest request){

        authService.login(loginDto.getEmail(),loginDto.getPassword(),request);

        return ApiResponse.of("SUCCESS",null);
    }

    @DeleteMapping
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        SecurityContextHolder.clearContext();

        return "로그아웃 성공";
    }

}
