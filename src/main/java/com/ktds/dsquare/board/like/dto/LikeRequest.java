package com.ktds.dsquare.board.like.dto;

import com.ktds.dsquare.board.enums.BoardType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LikeRequest {

    private BoardType boardType; //question:0, answer:1, card:2, talk:3, carrot:4
    private Long postId;

}
