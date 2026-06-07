package com.example.demo.Service;

import com.example.demo.Entity.Post;
import com.example.demo.Entity.User;
import com.example.demo.Repository.PostRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.dto.PostRequestDto;
import com.example.demo.dto.PostResponseDto;
import com.example.demo.dto.PostUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createPost(PostRequestDto postdto, Long loginUserId){

        User user = userRepository.findById(loginUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Post post = Post.builder()
                .title(postdto.getTitle())
                .body(postdto.getBody())
                .postImageUrl(postdto.getPostImageUrl())
                .user(user)
                .build();

        postRepository.save(post);
    }

    @Transactional
    public Slice<PostResponseDto> getPostList(Long lastPostId, int size) {
        PageRequest pageRequest = PageRequest.of(0, size);

        Slice<Post> postSlice = postRepository.findByIdLessThanOrderByIdDesc(lastPostId, pageRequest);

        return postSlice.map(post -> PostResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .authorNickname(post.getUser().getNickname())
                .authorProfileImage(post.getUser().getProfileImageUrl())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .replyCount(post.getReplyCount())
                .createdTime(post.getCreatedAt())
                .build()
        );
    }

    @Transactional
    public PostResponseDto getPostDetail(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        return PostResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .body(post.getBody()) // 상세 페이지니까 본문(body) 포함!
                .postImage(post.getPostImageUrl()) // 상세 페이지니까 이미지 포함!
                .authorNickname(post.getUser().getNickname())
                .authorProfileImage(post.getUser().getProfileImageUrl())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .replyCount(post.getReplyCount())
                .createdTime(post.getCreatedAt())
                .build();

    }

    @Transactional
    public void deletePost(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        postRepository.delete(post);
    }


    @Transactional
    public void updatePost(Long postId, PostUpdateRequestDto requestDto, Long loginUserId) {


        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));


        if (!post.getUser().getId().equals(loginUserId)) {
            throw new IllegalArgumentException("본인이 작성한 게시글만 수정할 수 있습니다.");
        }

        post.updatePost(
                requestDto.getTitle(),
                requestDto.getBody(),
                requestDto.getPostImageUrl()
        );
    }
}
