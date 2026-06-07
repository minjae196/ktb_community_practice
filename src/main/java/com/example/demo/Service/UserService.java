package com.example.demo.Service;

import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.dto.SignupRequestDto;
import com.example.demo.dto.UpdatedRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void SignUp(SignupRequestDto requestDto){
       if(userRepository.existsByEmail(requestDto.getEmail())){
           throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
       }
       if (!requestDto.getPassword().equals(requestDto.getPasswordCheck())){
           throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
       }

       User user = User.builder()
               .email(requestDto.getEmail())
               .password(requestDto.getPassword())
               .nickname(requestDto.getNickname())
               .profileImageUrl(requestDto.getProfileImageUrl())
               .build();

        userRepository.save(user);
    }

    //유저 삭제
    @Transactional
    public void deleteUser(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        userRepository.delete(user);
    }

    //유저정보 수정
    @Transactional
    public void updateUserInfo(Long id, UpdatedRequestDto updatedto){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        user.updateNickname(updatedto.getNickname());
        user.updateProfileImage(updatedto.getProfileImageUrl());
    }

    //비밀번호 수
    @Transactional
    public void updateUserPassword(Long id, UpdatedRequestDto updatedto){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        if(!updatedto.getPassword().equals(updatedto.getPasswordCheck())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        user.updatePassword(updatedto.getPassword());
    }


    public Long login(String email, String password){
        User user = userRepository.findByEmail(email)
                .orElse(null);

        if(user == null || !user.getPassword().equals(password)){
            return null;
        }
        return user.getId();
    }
}
