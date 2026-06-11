package com.example.demo.repository;

import com.example.demo.Entity.Count;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountRepository extends JpaRepository<Count, Integer> {
}
