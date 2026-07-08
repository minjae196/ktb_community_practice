package com.example.demo.config.filter;

import com.example.demo.Entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Integer userId;
    private final String email;
    private final String password;
    private final String nickname;
    private final String profileImageUrl;

    public CustomUserDetails(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
        this.profileImageUrl = user.getProfileImageUrl();
    }

    public Integer getUserId(){
        return userId;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 설정 (일단 빈 리스트로 둡니다)
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email; // 로그인에 사용할 ID (이메일)
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}
