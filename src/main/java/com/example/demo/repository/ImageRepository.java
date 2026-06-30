package com.example.demo.repository;

import com.example.demo.Entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<PostImage,Integer> {
    List<PostImage> findByPostId(Integer postId);
}
