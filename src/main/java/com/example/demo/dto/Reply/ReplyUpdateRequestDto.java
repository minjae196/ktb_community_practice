package com.example.demo.dto.Reply;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReplyUpdateRequestDto {

    @NotBlank
    private String comment;


}
