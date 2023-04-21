package com.ktds.dsquare.board.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardRequest {

    private Long cardWriterId;
    private Long projTeamId;
    private String title;
    private String content;
    private Integer teammateCnt;
    private String teammate;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private Long viewCnt;
    private Boolean deleteYn;

}
