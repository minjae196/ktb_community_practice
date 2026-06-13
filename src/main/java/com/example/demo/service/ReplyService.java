package com.example.demo.service;

import com.example.demo.Entity.Post;
import com.example.demo.Entity.Reply;
import com.example.demo.Entity.User;
import com.example.demo.event.ReplyEvent;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.ReplyRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.dto.reply.ReplyRequestDto;
import com.example.demo.dto.reply.ReplyResponseDto;
import com.example.demo.dto.reply.ReplyUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ApplicationEventPublisher eventPublisher;


    @Transactional
    public void createReply(Integer postId, ReplyRequestDto request,Integer loginUserId){
        User user = userRepository.findById(loginUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        Reply reply = Reply.builder()
                .comment(request.getComment())
                .user(user)
                .post(post)
                .build();

        replyRepository.save(reply);

        eventPublisher.publishEvent(new ReplyEvent(postId,true));
    }

    @Transactional(readOnly = true)
    public List<ReplyResponseDto> getReplies(Integer postId){
        List<Reply> replies = replyRepository.findAllByPost_Id(postId);

        return replies.stream()
                .map(ReplyResponseDto::new)
                .toList();
    }

    @Transactional
    public void updateReply(Integer postId, Integer replyId, ReplyUpdateRequestDto request, Integer loginUserId){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!reply.getUser().getId().equals(loginUserId)) {
            throw new IllegalArgumentException("본인이 작성한 게시글만 수정할 수 있습니다.");
        }

        reply.updateReply(
                request.getComment()
        );

    }

    @Transactional
    public void deleteReply(Integer replyId,Integer loginUserId){

        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if(!reply.getUser().getId().equals(loginUserId)){
            throw new IllegalArgumentException("본인이 작성한 댓글만 삭제할 수 있습니다.");

        }
        Integer postId = reply.getPost().getId();
        replyRepository.delete(reply);

        eventPublisher.publishEvent(new ReplyEvent(postId,false));
    }

}
