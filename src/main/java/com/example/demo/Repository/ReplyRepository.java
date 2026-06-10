package com.example.demo.Repository;

import com.example.demo.Entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply,Integer> {
    List<Reply> findAllByPost_Id(Integer postId);
}
