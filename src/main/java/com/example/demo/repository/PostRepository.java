package com.example.demo.repository;

import com.example.demo.Entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Integer> {

    Slice<Post> findByIdLessThanOrderByIdDesc(Integer lastPostId, Pageable pageable);
}
