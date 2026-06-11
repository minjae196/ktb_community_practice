package com.example.demo.config;

import com.example.demo.Entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Configuration
@Profile("development")
@RequiredArgsConstructor
public class SeedConfig {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner seedRunner(){
        return arguments -> seed();
    }

    @Transactional
    void seed() {
        if(userRepository.count() >= 10) return;

        IntStream.rangeClosed(1, 10).forEach(i -> {
            String rawPassword = "12341234aS!" + i;
            User user = new User("tester" + i + "@adapterz.kr", passwordEncoder.encode(rawPassword), "tester" + i,null);
            userRepository.save(user);
        });
    }
}
