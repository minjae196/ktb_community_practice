package com.example.demo.domain.user.Service;

import com.example.demo.domain.user.Dto.UserDto;
import com.example.demo.domain.user.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public UserDto signUp(UserDto user){
       if(userRepository.existByUserId(user.getUserEmail())){
           throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
       }

       return userRepository.insertUser(user);
    }

    public List<UserDto> getAllUsers(){
        return userRepository.getAllUsers();
    }

    public UserDto getUserByUserId(String id){
        return userRepository.findByUserId(id);
    }

    public void deleteUser(String id){
        userRepository.deleteUser(id);
    }

    public UserDto upadateUserInfo(String userId, UserDto updateDto){
        UserDto existingUser = userRepository.findByUserId(userId);
        if(existingUser == null){
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }

        existingUser.setUserEmail(updateDto.getUserEmail());
        userRepository.updateUser(existingUser);

        return existingUser;
    }

    public void updatePassword(String userId, String oldPassword, String newPassword){
        UserDto existingUser = userRepository.findByUserId(userId);
        if(existingUser == null){
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }

        if(!existingUser.getUserPassword().equals(oldPassword)){
            throw new IllegalArgumentException("기존 비밀번호가 일치하지 않습니다.");
        }

        existingUser.setUserPassword(newPassword);
        userRepository.updatePassword(existingUser);
    }
}
