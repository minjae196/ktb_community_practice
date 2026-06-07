package com.example.demo.Repository;

import com.example.demo.Entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Slice<Post> findByIdLessThanOrderByIdDesc(Long lastPostId, Pageable pageable);
}
