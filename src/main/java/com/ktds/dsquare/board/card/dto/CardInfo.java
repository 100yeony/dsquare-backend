package com.ktds.dsquare.board.card.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ktds.dsquare.board.card.Card;
import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
import com.ktds.dsquare.member.dto.response.TeamInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardInfo {

    private long cardId;
    private BriefMemberInfo writerInfo;
    private TeamInfo projTeamInfo;
    private String title;
    private String content;

    private int teammateCnt;
    private String teammates;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+9")
    private LocalDateTime createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+9")
    private LocalDateTime lastUpdateDate;

    private long viewCnt;
    private long likeCnt;
    private long commentCnt;

    private boolean likeYn;

    private CardSelectionInfo selectionInfo;


    public static CardInfo toDto(
            Card entity,
            long commentCnt,
            boolean likeYn
    ) {
        return CardInfo.builder()
                .cardId(entity.getId())
                .writerInfo(BriefMemberInfo.toDto(entity.getWriter()))
                .projTeamInfo(TeamInfo.toDto(entity.getProjTeam()))
                .title(entity.getTitle())
                .content(entity.getContent())
                .teammateCnt(entity.getTeammateCnt())
                .teammates(entity.getTeammates().toString())
                .createDate(entity.getCreateDate())
                .lastUpdateDate(entity.getLastUpdateDate())
                .viewCnt(entity.getViewCnt())
                .likeCnt(entity.getLikeCnt())
                .commentCnt(commentCnt)
                .likeYn(likeYn)
                .selectionInfo(CardSelectionInfo.toDto(entity))
                .build();
    }

}
