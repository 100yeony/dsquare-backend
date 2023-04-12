package com.ktds.dsquare.board.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AnswerRequest {

    private Long id;
    //member 타입으로 변경해서 이름, 닉네임 가져오기
    private Long writerId;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private String content;
    private Long atcId;
    private Boolean deleteYn;
    private Long qid;
}
