package com.example.demo.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostViewEvent {
    private final Integer postId;
}
