package com.example.demo.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LikeEvent {
    private final Integer postId;
    private final boolean isLiked;
}
