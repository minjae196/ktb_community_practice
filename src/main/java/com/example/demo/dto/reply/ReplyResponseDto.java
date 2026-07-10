package com.example.demo.dto.reply;

import com.example.demo.Entity.Reply;
import lombok.Getter;

@Getter
public class ReplyResponseDto {

    private Integer replyId;
    private String comment;
    private String authorNickname;
    private String authorProfileImage;

    public ReplyResponseDto(Reply reply) {
        this.replyId = reply.getId();
        this.comment = reply.getComment();
        // User 엔티티에서 닉네임이나 이름을 가져오기
        this.authorNickname = reply.getUser().getNickname();
        this.authorProfileImage = reply.getUser().getProfileImageUrl();
    }

}
