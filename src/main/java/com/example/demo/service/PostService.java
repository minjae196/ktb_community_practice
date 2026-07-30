package com.example.demo.service;

import com.example.demo.Entity.Count;
import com.example.demo.Entity.Post;
import com.example.demo.Entity.PostImage;
import com.example.demo.Entity.User;
import com.example.demo.event.PostViewEvent;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.dto.post.PostRequestDto;
import com.example.demo.dto.post.PostResponseDto;
import com.example.demo.dto.post.PostUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final S3ImageService s3ImageService;

    @Transactional
    public Integer createPost(PostRequestDto postDto, Integer loginUserId){

        User user = userRepository.findById(loginUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Post post = Post.builder()
                .title(postDto.getTitle())
                .body(postDto.getBody())
                .user(user)
                .createdTime(LocalDateTime.now())
                .build();

        Count count = Count.builder()
                .post(post)
                .viewCount(0)
                .replyCount(0)
                .likeCount(0)
                .build();

        post.setCount(count);
        Post savedPost = postRepository.save(post);

        if (postDto.getPostImageUrl() != null && !postDto.getPostImageUrl().isEmpty()) {
            PostImage postImage = new PostImage(post, postDto.getPostImageUrl());
            imageRepository.save(postImage);
        }
        return savedPost.getId();

    }

    @Transactional(readOnly = true)
    public Slice<PostResponseDto> getPostList(Integer lastPostId, int size) {
        PageRequest pageRequest = PageRequest.of(0, size);

        Slice<Post> postSlice = postRepository.findPostsWithFetchJoin(lastPostId, pageRequest);

        return postSlice.map(post -> PostResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .authorNickname(post.getUser().getNickname())
                .authorProfileImage(post.getUser().getProfileImageUrl())
                .viewCount(post.getCount() != null ? post.getCount().getViewCount() : 0)
                .likeCount(post.getCount() != null ? post.getCount().getLikeCount() : 0)
                .replyCount(post.getCount() != null ? post.getCount().getReplyCount() : 0)
                .createdTime(post.getCreatedTime())
                .build()
        );
    }

    @Transactional
    public PostResponseDto getPostDetail(Integer postId, Integer currentUserId){
        Post post = postRepository.findById(postId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        boolean isLiked = false;
        // Post 엔티티 안에 있는 likes 리스트를 뒤져서, 현재 로그인한 유저의 ID가 있는지 확인
        if(currentUserId != null){
            isLiked = post.getLikes().stream()
                    .anyMatch(like -> like.getUser().getId().equals(currentUserId));
        }

        eventPublisher.publishEvent(new PostViewEvent(postId));

        List<PostImage> images = imageRepository.findByPostId(postId);

        List<String> imageUrls = images.stream()
                .map(PostImage::getPostImageUrl)
                .collect(Collectors.toList());

        return PostResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .body(post.getBody()) // 상세 페이지니까 본문(body) 포함
                .postImages(imageUrls)
                .authorId(post.getUser().getId())
                .authorNickname(post.getUser().getNickname())
                .authorProfileImage(post.getUser().getProfileImageUrl())
                .viewCount(post.getCount() != null ? post.getCount().getViewCount() : 0)
                .likeCount(post.getCount() != null ? post.getCount().getLikeCount() : 0)
                .replyCount(post.getCount() != null ? post.getCount().getReplyCount() : 0)
                .isLiked(isLiked)
                .createdTime(post.getCreatedTime())
                .build();

    }

    @Transactional
    public void deletePost(Integer postId, Integer loginUserId){
        Post post = postRepository.findById(postId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        if(!post.getUser().getId().equals(loginUserId)){
            throw new IllegalArgumentException("본인이 작성한 게시글만 삭제할 수 있습니다.");
        }

        List<String> imageKeys = post.getPostImages().stream()
                .map(image -> {
                    String url = image.getPostImageUrl();
                    // "amazonaws.com/" 이후의 문자열(실제 경로 및 파일명)만 잘라내기
                    int splitIndex = url.indexOf(".amazonaws.com/") + 15;
                    return url.substring(splitIndex);
                })
                .toList();

        postRepository.delete(post);

        for(String key : imageKeys){
            s3ImageService.deleteFile(key);

            if (key.endsWith(".webp")) {
                String pngKey = key.replace(".webp", ".png");
                s3ImageService.deleteFile(pngKey);
            }
        }
    }


    @Transactional
    public void updatePost(Integer postId, PostUpdateRequestDto requestDto, Integer loginUserId) {


        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));


        if (!post.getUser().getId().equals(loginUserId)) {
            throw new IllegalArgumentException("본인이 작성한 게시글만 수정할 수 있습니다.");
        }

        List<String> oldImageUrls = post.getPostImages().stream()
                .map(PostImage::getPostImageUrl)
                .toList();

        post.updatePost(
                requestDto.getTitle(),
                requestDto.getBody()
        );

        imageRepository.deleteAll(post.getPostImages());

        post.getPostImages().clear();

        String newImageUrl = requestDto.getPostImageUrl();
        if (newImageUrl != null && !newImageUrl.isBlank()) {
            imageRepository.save(new PostImage(post, newImageUrl));
        }

        for (String oldImageUrl : oldImageUrls) {
            if (oldImageUrl.equals(newImageUrl)) {
                continue;
            }

            String key = extractS3Key(oldImageUrl);
            if (key == null) {
                continue;
            }
            s3ImageService.deleteFile(key);

            if (key.endsWith(".webp")) {
                String pngKey = key.replace(".webp", ".png");
                s3ImageService.deleteFile(pngKey);
            }
        }
    }

    private String extractS3Key(String url) {
        if (url == null || url.isBlank()) {
            return null;
        }

        int splitIndex = url.indexOf(".amazonaws.com/");
        if (splitIndex < 0) {
            return null;
        }

        return url.substring(splitIndex + ".amazonaws.com/".length());
    }
}
