package com.example.demo.domain.user.Repository;

import com.example.demo.domain.user.Dto.UserDto;

import java.util.List;

public interface UserRepository {
    UserDto insertUser(UserDto user);
    UserDto findByUserId(String id);
    UserDto findByEmail(String email);
    boolean existByUserId(String id);
    List<UserDto> getAllUsers();
    void deleteUser(String id);
    void updateUser(UserDto user);
    void updatePassword(UserDto user);
}
