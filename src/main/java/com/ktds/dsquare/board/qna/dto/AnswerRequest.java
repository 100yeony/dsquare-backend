package com.ktds.dsquare.board.qna.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Builder
@NoArgsConstructor @AllArgsConstructor
public class AnswerRequest {

    private Long id;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private String content;
    private Long atcId;
    private Boolean deleteYn;
    private Long qid;
}
