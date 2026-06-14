package com.example.demo.repository;

import com.example.demo.Entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post,Integer> {

    //Slice<Post> findByIdLessThanOrderByIdDesc(Integer lastPostId, Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "JOIN FETCH p.user " +
            "JOIN FETCH p.count " +
            "WHERE p.id < :lastPostId " +
            "ORDER BY p.id DESC")
    Slice<Post> findPostsWithFetchJoin(@Param("lastPostId") Integer lastPostId, Pageable pageable);
}
