package com.example.demo.Config;

import com.example.demo.Entity.Post;
import com.example.demo.Entity.Reply;
import com.example.demo.Entity.User;
import com.example.demo.Repository.PostRepository;
import com.example.demo.Repository.ReplyRepository;
import com.example.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class SeedConfig {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;

    @Bean
    ApplicationRunner seedRuner(){
        return args -> seed();
    }

    @Transactional
    void seed() {
        if (userRepository.count() >= 10 && postRepository.count() >= 10) return;

        IntStream.rangeClosed(1, 10).forEach(i -> {
            User user = new User("tester"+i+"@adapterz.kr", "123aS!"+i, "tester"+i,"https://image.kr/img" + i + ".jpg" );
            userRepository.save(user);

            Post post = new Post("title"+i, "content"+i,
                    "https://image.kr/img" + i + ".jpg", user);
            postRepository.save(post);

            Reply reply = new Reply("good" + i, post,user);
            replyRepository.save(reply);
        });
    }
}
