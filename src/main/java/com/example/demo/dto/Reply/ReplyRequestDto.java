package com.example.demo.dto.Reply;

import com.example.demo.Entity.Post;
import com.example.demo.Entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReplyRequestDto {
    @NotBlank
    private String comment;

}
