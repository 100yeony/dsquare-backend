package com.ktds.dsquare.board.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    private List<String> tags;

}
