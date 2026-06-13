package com.example.demo.repository;

import com.example.demo.Entity.Post;
import com.example.demo.Entity.PostLike;
import com.example.demo.Entity.PostLikeId;
import com.example.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {
    // 좋아요 여부를 확인하는 메서드
    boolean existsByUserAndPost(User user, Post post);

    // 좋아요 취소를 위한 삭제 메서드
    void deleteByUserAndPost(User user, Post post);
}
