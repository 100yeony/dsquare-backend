package com.ktds.dsquare.board.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NestedCommentRegisterDto {
    private String content;
    private Long originWriterId;
}
