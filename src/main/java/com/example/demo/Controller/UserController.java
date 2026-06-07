package com.example.demo.Controller;

import com.example.demo.dto.SignupRequestDto;
import com.example.demo.dto.UpdatedRequestDto;
import com.example.demo.dto.UserDto;
import com.example.demo.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public String signUp(@RequestBody SignupRequestDto requestDto) {
        userService.SignUp(requestDto);
        return "회원가입 성공";
    }


    @PatchMapping("/{userId}")
    public String updateUserInfo(@PathVariable Long userId, @RequestBody UpdatedRequestDto updateDto, HttpServletRequest request){

        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("Login_user")== null){
            throw new IllegalArgumentException("로그인이 필요한 서비스입니다.");
        }
        Long loginUserId = (Long) session.getAttribute("Login_user");
        if (!loginUserId.equals(userId)) {
            throw new IllegalArgumentException("본인의 프로필만 수정할 수 있습니다.");
        }
        userService.updateUserInfo(userId,updateDto);

        return "정보 수정 성공";
    }

    @PutMapping("/{userId}/password")
    public String updatePassword(@PathVariable Long userId, @RequestBody UpdatedRequestDto updateDto, HttpServletRequest request){
        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("Login_user")== null){
            throw new IllegalArgumentException("로그인이 필요한 서비스입니다.");
        }
        Long loginUserId = (Long) session.getAttribute("Login_user");
        if (!loginUserId.equals(userId)) {
            throw new IllegalArgumentException("본인의 비밀번호만 수정할 수 있습니다.");
        }

        userService.updateUserPassword(userId,updateDto);
        return "비밀번호 수정 성공";
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Long userId,HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("Login_user") == null){
            throw new IllegalArgumentException("로그인이 필요한 서비스입니다.");
        }

        Long loginUserId = (Long) session.getAttribute("Login_user");
        if (!loginUserId.equals(userId)) {
            throw new IllegalArgumentException("본인의 계정만 삭제할 수 있습니다.");
        }

        userService.deleteUser(userId);
        session.invalidate();
        return "회원 탈퇴 성공";
    }
}

