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

@Service
@RequiredArgsConstructor
public class LikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void toggleLike(Integer postId, Integer userId){
        User user = userRepository.findById(userId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();

        boolean isLiked = postLikeRepository.existsByUserAndPost(user,post);

        // 좋아요 이미 눌렀는지
        if(isLiked){
            postLikeRepository.deleteByUserAndPost(user,post);
        } else {
            PostLike postLike = new PostLike(user, post);
            postLikeRepository.save(postLike);
        }

        eventPublisher.publishEvent(new LikeEvent(postId,isLiked));


    }
}
