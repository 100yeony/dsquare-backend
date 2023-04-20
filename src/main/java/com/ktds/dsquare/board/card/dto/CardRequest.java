package com.ktds.dsquare.board.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CardRequest {

    private Long cardId;
    private Long cardWriterId;
    private Long projTeamId;
    private String title;
    private String content;
    private String teammate;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private Long viewCnt;
    private Boolean deleteYn;

}
