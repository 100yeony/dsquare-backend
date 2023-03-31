package com.ktds.dsquare.board.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AnswerDto {

    private Long id;
    private Long writerId;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private String content;
    private Long atcId;
    private Boolean deleteYn;
    private Long qid;

}
