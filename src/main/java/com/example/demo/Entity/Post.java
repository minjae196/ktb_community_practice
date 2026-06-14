package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String body;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdTime;

    @LastModifiedDate
    private LocalDateTime updatedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Count count;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostLike> likes = new ArrayList<>();

    @Builder
    public Post(String title, String body,User user,LocalDateTime createdTime){
        this.title = title;
        this.body = body;
        this.user = user;
        this.createdTime = createdTime;
    }

    public void updatePost(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public void setCount(Count count) {
        this.count = count;
    }

}
