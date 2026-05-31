package com.example.demo.domain.user.Controller;

import com.example.demo.domain.user.Dto.UserDto;
import com.example.demo.domain.user.Service.UserService;
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
    public UserDto updateUserInfo(@PathVariable String userId, @RequestBody UserDto updateDto){
        return userService.upadateUserInfo(userId,updateDto);
    }

    @PutMapping("/{userId}/password")
    public void updatePassword(@PathVariable String userId, @RequestParam String oldPassword, @RequestParam String newPassword){
        userService.updatePassword(userId,oldPassword,newPassword);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return ;
    }

}
