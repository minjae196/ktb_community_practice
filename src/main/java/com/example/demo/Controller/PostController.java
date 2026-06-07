package com.example.demo.Controller;

import com.example.demo.Entity.Post;
import com.example.demo.Service.PostService;
import com.example.demo.dto.PostRequestDto;
import com.example.demo.dto.PostResponseDto;
import com.example.demo.dto.PostUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;


    @PostMapping()
    public String createPost(
            @RequestBody PostRequestDto requestDto,
            @SessionAttribute(name = "Login_user") Long loginUserId) {

        postService.createPost(requestDto, loginUserId);

        return "게시물 생성 성공";

    }

    @GetMapping()
    public Slice<PostResponseDto> getPosts(
            @RequestParam(value = "lastId", required = false) Long lastId,
            @RequestParam(value = "size", defaultValue = "10") int size){

        Long searchId = (lastId == null) ? Long.MAX_VALUE : lastId;

        return postService.getPostList(searchId, size);
    }

    @GetMapping("/{postId}")
    public PostResponseDto getPostDetail(@PathVariable Long postId){
        return postService.getPostDetail(postId);
    }


    @PatchMapping("/{postId}")
    public String updatePost(
            @PathVariable Long postId,
            @RequestBody PostUpdateRequestDto requestDto,
            @SessionAttribute(name = "Login_user", required = false) Long loginUserId) {

        if (loginUserId == null) {
            throw new IllegalArgumentException("로그인이 필요한 서비스입니다.");
        }

        postService.updatePost(postId, requestDto, loginUserId);

        return "게시글 수정 완료";
    }

    @DeleteMapping("/{postId}")
    public String deletePost(@PathVariable Long postId,
                             @SessionAttribute(name = "Login_user", required = false) Long loginUserId
    ){
        postService.deletePost(postId, loginUserId);

        return "게시물 삭제 성공";
    }
}
