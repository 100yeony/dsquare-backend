package com.ktds.dsquare.board.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LikeRegisterRequest {

    private String boardType;
    private Long postId;

}
