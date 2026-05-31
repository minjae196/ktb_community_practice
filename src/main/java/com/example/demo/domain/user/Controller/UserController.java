package com.example.demo.domain.user.Controller;

import com.example.demo.domain.user.Dto.UserDto;
import com.example.demo.domain.user.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public UserDto signUp(@RequestBody UserDto user){
        return userService.signUp(user);
    }

    @GetMapping
    public List<UserDto> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserByUserId(@PathVariable String userId){
        return userService.getUserByUserId(userId);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUserInfo(@PathVariable String userId, @RequestBody UserDto updateDto, HttpServletRequest request){

        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("Login_user")== null){
            throw new IllegalArgumentException("로그인이 필요한 서비스입니다.");
        }
        String loginUserId = (String) session.getAttribute("Login_user");
        if (!loginUserId.equals(userId)) {
            throw new IllegalArgumentException("본인의 프로필만 수정할 수 있습니다.");
        }
        return userService.updateUserInfo(userId,updateDto);
    }

    @PutMapping("/{userId}/password")
    public void updatePassword(@PathVariable String userId, @RequestParam String oldPassword, @RequestParam String newPassword, HttpServletRequest request){
        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("Login_user")== null){
            throw new IllegalArgumentException("로그인이 필요한 서비스입니다.");
        }
        String loginUserId = (String) session.getAttribute("Login_user");
        if (!loginUserId.equals(userId)) {
            throw new IllegalArgumentException("본인의 비밀번호만 수정할 수 있습니다.");
        }

        userService.updatePassword(userId,oldPassword,newPassword);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return ;
    }

}
