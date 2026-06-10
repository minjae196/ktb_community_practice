package com.example.demo.Controller;

import com.example.demo.Entity.Reply;
import com.example.demo.Repository.ReplyRepository;
import com.example.demo.Service.ReplyService;
import com.example.demo.dto.Reply.ReplyRequestDto;
import com.example.demo.dto.Reply.ReplyResponseDto;
import com.example.demo.dto.Reply.ReplyUpdateRequestDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/{postid}/replies")
    public void createReply(@PathVariable Integer postId,
                              @RequestBody ReplyRequestDto request,
                              @SessionAttribute(name = "Login_user", required = false) Integer loginUserId){

        replyService.createReply(postId, request, loginUserId);
    }

    @GetMapping("/{postid}/replies")
    public ResponseEntity<List<ReplyResponseDto>> getReplies(@PathVariable Integer postId){
        List<ReplyResponseDto> responseDtos = replyService.getReplies(postId);

        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/{postId}/{replyId}")
    public ResponseEntity<String> updateReply(
            @PathVariable Integer postId,
            @PathVariable Integer replyId,
            @RequestBody ReplyUpdateRequestDto request,
            @SessionAttribute(name = "Login_user", required = false) Integer loginUserId){

        replyService.updateReply(postId, replyId, request, loginUserId);

        return ResponseEntity.ok("댓글 수정 완 ");
    }

    @DeleteMapping("/post/{replyId}")
    public ResponseEntity<String> deleteReply(
            @PathVariable Integer replyId,
            @SessionAttribute(name = "Login_user", required = false) Integer loginUserId){

        replyService.deleteReply(replyId,loginUserId);

        return ResponseEntity.ok("댓글 삭제 완료.");
    }

}


