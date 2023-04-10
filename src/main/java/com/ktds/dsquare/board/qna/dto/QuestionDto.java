package com.ktds.dsquare.board.qna.dto;

import com.ktds.dsquare.board.qna.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class QuestionDto {

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
