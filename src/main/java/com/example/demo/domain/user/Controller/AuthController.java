package com.example.demo.domain.user.Controller;

import com.example.demo.domain.user.Dto.UserDto;
import com.example.demo.domain.user.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpServletRequest request){
        UserDto user = userService.login(email, password);

        if (user == null){
            return "로그인 실패";
        }

        HttpSession session = request.getSession(true);

        session.setAttribute("Login_user",user.getUserId());

        return "로그인 성공";
    }

}
