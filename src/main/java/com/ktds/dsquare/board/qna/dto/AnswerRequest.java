package com.ktds.dsquare.board.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bytebuddy.asm.Advice;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AnswerRequest {

    private Long id;
    private Long questionId;
    private Long writerId;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private String content;
    private Long atcId;
    private Boolean deleteYn;

}
