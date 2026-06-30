package com.example.demo.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private Integer postId;
    private String title;
    private String body;
    private List<String> postImages;
    private Integer authorId;
    private String authorNickname;
    private String authorProfileImage;

    private int viewCount;
    private int likeCount;
    private int replyCount;

    @JsonProperty("isLiked")
    private boolean isLiked;

    private LocalDateTime createdTime;

}
