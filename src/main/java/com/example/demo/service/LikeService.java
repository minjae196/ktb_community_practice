package com.example.demo.service;

import com.example.demo.Entity.Post;
import com.example.demo.Entity.PostLike;
import com.example.demo.Entity.User;
import com.example.demo.event.LikeEvent;
import com.example.demo.repository.PostLikeRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void addLike(Integer postId, Integer userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();

        // 중복 방지
        if (postLikeRepository.findByUserAndPost(user, post).isPresent()) {
            throw new IllegalArgumentException("이미 좋아요를 누른 게시글입니다.");
        }

        PostLike postLike = new PostLike(user, post);
        postLikeRepository.save(postLike);

        // 이벤트 발행 (카운트 증가)
        eventPublisher.publishEvent(new LikeEvent(postId, true));
    }


    @Transactional
    public void removeLike(Integer postId, Integer userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();

        // 누른 적이 있는 좋아요인지 조회
        PostLike existingLike = postLikeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new IllegalArgumentException("좋아요를 누르지 않은 게시글입니다."));

        // 좋아요 삭제
        postLikeRepository.delete(existingLike);

        // 이벤트 발행
        eventPublisher.publishEvent(new LikeEvent(postId, false));
    }
}
