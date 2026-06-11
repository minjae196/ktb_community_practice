package com.example.demo.controller;

import com.example.demo.config.filter.CustomUserDetails;
import com.example.demo.service.ReplyService;
import com.example.demo.dto.reply.ReplyRequestDto;
import com.example.demo.dto.reply.ReplyResponseDto;
import com.example.demo.dto.reply.ReplyUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/{postId}/replies")
    public void createReply(@PathVariable Integer postId,
                            @RequestBody ReplyRequestDto request,
                            @AuthenticationPrincipal CustomUserDetails userDetails){

        replyService.createReply(postId, request, userDetails.getUserId());
    }

    @GetMapping("/{postId}/replies")
    public ResponseEntity<List<ReplyResponseDto>> getReplies(@PathVariable Integer postId){
        List<ReplyResponseDto> responseDtos = replyService.getReplies(postId);

        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/{postId}/{replyId}")
    public ResponseEntity<String> updateReply(
            @PathVariable Integer postId,
            @PathVariable Integer replyId,
            @RequestBody ReplyUpdateRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails){

        replyService.updateReply(postId, replyId, request, userDetails.getUserId());

        return ResponseEntity.ok("댓글 수정 완 ");
    }

    @DeleteMapping("/post/{replyId}")
    public ResponseEntity<String> deleteReply(
            @PathVariable Integer replyId,
            @AuthenticationPrincipal CustomUserDetails userDetails){

        replyService.deleteReply(replyId, userDetails.getUserId());

        return ResponseEntity.ok("댓글 삭제 완료.");
    }

}


