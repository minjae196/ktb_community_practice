package com.example.demo.Config;

import com.example.demo.Entity.Count;
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
            // 1. 유저 생성 및 저장
            User user = new User("tester"+i+"@adapterz.kr", "123aS!"+i, "tester"+i,"https://image.kr/img" + i + ".jpg" );
            userRepository.save(user);

            // 2. 게시글 생성
            Post post = new Post("title"+i, "content"+i, user);

            // 🌟 3. 수정한 빌더로 Count 생성 (Post 객체를 직접 주입!)
            Count count = Count.builder()
                    .post(post)
                    .viewCount(0)
                    .replyCount(0)
                    .likeCount(0)
                    .build();

            // 🌟 4. Post에 Count를 연결 (CascadeType.ALL 작동을 위해 필수)
            post.assignCount(count);

            // 5. Post를 저장하면 Cascade에 의해 Count도 자동으로 세트 저장됨!
            postRepository.save(post);

            // 6. 댓글 생성 및 저장
            Reply reply = new Reply("good" + i, post, user);
            replyRepository.save(reply);
        });
    }
}
