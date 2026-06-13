package com.example.demo.config;

import com.example.demo.Entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class SeedConfig {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // DB에 유저가 10명 미만일 때만 데이터를 채워 넣습니다.
            if (userRepository.count() < 10) {
                IntStream.rangeClosed(1, 10).forEach(i -> {
                    String rawPassword = "12341234aS!" + i;

                    User user = User.builder()
                            .email("tester" + i + "@test.com")
                            .password(passwordEncoder.encode(rawPassword))
                            .nickname("테스터" + i)
                            .build();

                    userRepository.save(user);
                });

                // ✨ 서버 켜질 때 콘솔창에 잘 들어갔는지 띄워줍니다!
                System.out.println("🌱 초기 유저 10명 세팅이 완료되었습니다!");
            } else {
                System.out.println("🌿 이미 유저 데이터가 존재하여 Seed를 건너뜁니다.");
            }
        };
    }
}
