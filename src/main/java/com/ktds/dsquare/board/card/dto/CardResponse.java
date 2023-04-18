package com.ktds.dsquare.board.card.dto;

import com.ktds.dsquare.board.card.Card;
import com.ktds.dsquare.member.dto.response.MemberInfo;
import com.ktds.dsquare.member.dto.response.TeamInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardResponse {

    private Long cardId;
    private MemberInfo writerInfo;
    private TeamInfo projTeamInfo;
    private String title;
    private String content;
    private String teammate;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private Long viewCnt;
    private CardSelectionInfo selectionInfo;

    public static CardResponse toDto(Card entity, MemberInfo writerInfo, TeamInfo teamInfo, CardSelectionInfo selectionInfo) {
        if(selectionInfo == null){
            selectionInfo = null;
        }
        return CardResponse.builder()
                .cardId(entity.getCardId())
                .writerInfo(writerInfo)
                .projTeamInfo(teamInfo)
                .title(entity.getTitle())
                .content(entity.getContent())
                .teammate(entity.getTeammate())
                .createDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
                .viewCnt(entity.getViewCnt())
                .selectionInfo(selectionInfo)
                .build();
    }


}
