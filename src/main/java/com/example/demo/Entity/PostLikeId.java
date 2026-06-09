package com.example.demo.Entity;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode
public class PostLikeId implements Serializable {
    private Integer user;
    private Long post;
}
