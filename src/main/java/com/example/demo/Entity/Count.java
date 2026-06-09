package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Count {

    @Id
    @Column(name = "postId")
    private Integer postId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    private int viewCount = 0;

    @Column(nullable = false)
    private int replyCount = 0;

    @Column(nullable = false)
    private int likeCount = 0;

}
