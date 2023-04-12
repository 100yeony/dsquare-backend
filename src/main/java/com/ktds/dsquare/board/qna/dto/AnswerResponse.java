package com.ktds.dsquare.board.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponse {
    private Long id;
    //member 이름, 닉네임 가져오기
    private Long writerId;
    private String writerName;
    private String writerNickname;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private String content;
    private Long atcId;
    private Boolean deleteYn;
    private Long qid;
}
