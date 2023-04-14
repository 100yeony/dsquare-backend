package com.ktds.dsquare.board.qna.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Builder
@NoArgsConstructor @AllArgsConstructor
public class QuestionRequest {

    private Long qid;
    private Long writerId;
    private Integer cid;
    private String title;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private Long viewCnt;
    private Long atcId;
    private Boolean deleteYn;

}
