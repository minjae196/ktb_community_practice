package com.example.demo.controller;

import com.example.demo.config.filter.CustomUserDetails;
import com.example.demo.service.LikeService;
import com.example.demo.service.PostService;
import com.example.demo.dto.post.PostRequestDto;
import com.example.demo.dto.post.PostResponseDto;
import com.example.demo.dto.post.PostUpdateRequestDto;
import com.example.demo.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final LikeService likeService;


    @PostMapping()
    public ResponseEntity<ApiResponse<Void>> createPost(
            @RequestBody PostRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        postService.createPost(requestDto, userDetails.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<Void>of("POST_CREATE_SUCCESS",null));
    }

    @GetMapping()
    public Slice<PostResponseDto> getPosts(
            @RequestParam(value = "lastId", required = false) Integer lastId,
            @RequestParam(value = "size", defaultValue = "10") int size){

        Integer searchId = (lastId == null) ? Integer.MAX_VALUE : lastId;

        return postService.getPostList(searchId, size);
    }

    @GetMapping("/{postId}")
    public PostResponseDto getPostDetail(@PathVariable Integer postId){
        return postService.getPostDetail(postId);
    }


    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> updatePost(
            @PathVariable Integer postId,
            @RequestBody PostUpdateRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        postService.updatePost(postId, requestDto, userDetails.getUserId());

        return ResponseEntity.ok(ApiResponse.<Void>of("POST_UPDATE_SUCCESS",null));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Integer postId,
                             @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        postService.deletePost(postId, userDetails.getUserId());

        return ResponseEntity.ok(ApiResponse.<Void>of("POST_DELETE_SUCCESS",null));
    }

    @PostMapping("/{postId}/likes")
    public ResponseEntity<ApiResponse<Void>> toggleLike(
            @PathVariable Integer postId,
            @AuthenticationPrincipal CustomUserDetails userDetails){

        likeService.toggleLike(postId,userDetails.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of("LIKE_CREATE_SUCCESS",null));
    }

}
