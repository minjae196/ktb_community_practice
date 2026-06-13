package com.example.demo.repository;

import com.example.demo.Entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<PostImage,Integer> {
}
