package com.example.demo.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReplyEvent {
    private final Integer postId;
    private final boolean isAdded;
}
