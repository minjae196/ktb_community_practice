package com.example.demo.Controller;

import com.example.demo.dto.Auth.LoginRequestDto;
import com.example.demo.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping
    public String login(@RequestBody LoginRequestDto loginDto,
                        HttpServletRequest request){

        Integer userId = userService.login(loginDto.getEmail(), loginDto.getPassword());


        HttpSession session = request.getSession(true);
        session.setAttribute("Login_user", userId);

        return "로그인 성공";
    }

    @DeleteMapping
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return "로그아웃 성공";
    }

}
