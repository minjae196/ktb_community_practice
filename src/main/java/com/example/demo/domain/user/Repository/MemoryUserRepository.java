package com.example.demo.domain.user.Repository;

import com.example.demo.domain.user.Dto.UserDto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Repository
public class MemoryUserRepository implements UserRepository {

    static public ArrayList<UserDto> users;

    static{
        users = new ArrayList<>();
        users.add(new UserDto("john", "a1","qwer@naver.com","1234","https://image.kr/img.jpg"));
        users.add(new UserDto("lukas","a2","asdf@naver.com","5678","https://image.kr/img.jpg"));
        users.add(new UserDto("stella","a3","zxcv@naver.com","12345678","https://image.kr/img.jpg"));
        users.add(new UserDto("ayden","a4","qazwsx@naver.com","1231413","https://image.kr/img.jpg"));
    }

    @Override
    public UserDto insertUser(UserDto user){
        users.add(user);
        return user;
    }
    @Override
    public UserDto findByUserId(String id){
        for(UserDto user: users){
            if(user.getUserId().equals(id)){
                return user;
            }
        }
        return null;
    }

    @Override
    public boolean existByUserId(String id){
        for(UserDto user:users){
            if(user.getUserId().equals(id)){
                return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<UserDto> getAllUsers(){
        return users;
    }

    @Override
    public void deleteUser(String id){
        users.removeIf(user -> user.getUserId().equals(id));
    }

    @Override
    public void updateUser(UserDto updatedUser){
        for(UserDto user:users){
            if(user.getUserId().equals(updatedUser.getUserId())){
                user.setUserNickname(updatedUser.getUserNickname());
                return;
            }
        }
    }

    @Override
    public void updatePassword(UserDto updatedUser){
        for(UserDto user:users){
            if(user.getUserId().equals(updatedUser.getUserId())){
                user.setUserPassword(updatedUser.getUserPassword());
                return;
            }
        }
    }
}
