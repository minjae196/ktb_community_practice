package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String nickname;


    private String profileImageUrl;


    @Builder
    public User(String email, String password, String nickname, String profileImageUrl){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }
    public void updateProfileImage(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }

    public void updatePassword(String password){
        this.password = password;
    }



}
