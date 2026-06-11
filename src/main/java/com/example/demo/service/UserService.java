package com.example.demo.service;

import com.example.demo.Entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.dto.user.SignupRequestDto;
import com.example.demo.dto.user.UserUpdatedRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
               .password(passwordEncoder.encode(requestDto.getPassword()))
               .nickname(requestDto.getNickname())
               .profileImageUrl(requestDto.getProfileImageUrl())
               .build();

        userRepository.save(user);
    }

    //유저 삭제
    @Transactional
    public void deleteUser(Integer id){
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        userRepository.delete(user);
    }

    //유저정보 수정
    @Transactional
    public void updateUserInfo(Integer id, UserUpdatedRequestDto updateDto){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        user.updateNickname(updateDto.getNickname());
        user.updateProfileImage(updateDto.getProfileImageUrl());
    }

    //비밀번호 수
    @Transactional
    public void updateUserPassword(Integer id, UserUpdatedRequestDto updateDto){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        if(!updateDto.getPassword().equals(updateDto.getPasswordCheck())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        user.updatePassword(passwordEncoder.encode(updateDto.getPassword()));
    }

}
