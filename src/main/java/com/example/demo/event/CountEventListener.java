package com.example.demo.event;

import com.example.demo.Entity.Count;
import com.example.demo.repository.CountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CountEventListener {
    private final CountRepository countRepository;

    @TransactionalEventListener
    public void handleViewCountEvent(PostViewEvent event){
        Count count = countRepository.findByPostId(event.getPostId());
        count.increaseViewCount();
    }

    @TransactionalEventListener
    public void handleLikeEvent(LikeEvent event) {
        Count count = countRepository.findByPostId(event.getPostId());
        if (event.isLiked()) count.increaseLikeCount();
        else count.decreaseLikeCount();
    }

    @TransactionalEventListener
    public void handleReplyEvent(ReplyEvent event) {
        Count count = countRepository.findByPostId(event.getPostId());
        if (event.isAdded()) {
            count.increaseReplyCount();
        } else {
            count.decreaseReplyCount();
        }
    }

}
