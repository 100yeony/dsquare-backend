package com.ktds.dsquare.board.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardRegisterRequest {

    private Long projTeamId;
    private String title;
    private String content;
    private Integer teammateCnt;
    private List<String> teammate;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private Long viewCnt;
    private Boolean deleteYn;

}
