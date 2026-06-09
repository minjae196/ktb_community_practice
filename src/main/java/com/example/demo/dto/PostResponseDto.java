package com.example.demo.dto;

import com.example.demo.Entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private Integer postId;
    private String title;
    private String body;
    private String postImage;
    private Long authorId;
    private String authorNickname;
    private String authorProfileImage;

    private int viewCount;
    private int likeCount;
    private int replyCount;

    private LocalDateTime createdTime;

}
