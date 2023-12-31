package com.ktds.dsquare.board.card.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ktds.dsquare.board.card.Card;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
import com.ktds.dsquare.member.dto.response.TeamInfo;
import com.ktds.dsquare.member.team.Team;
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
    private BriefMemberInfo writerInfo;
    private TeamInfo projTeamInfo;
    private String title;
    private String content;
    private Integer teammateCnt;
    private String teammates;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+9")
    private LocalDateTime createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+9")
    private LocalDateTime lastUpdateDate;
    private Long viewCnt;
    private Long likeCnt;
    private Boolean likeYn;
    private Long commentCnt;
    private CardSelectionInfo selectionInfo;

    public static CardResponse toDto(Card entity, Team team, Member cardOwner, Long likeCnt, Boolean likeYn, Long commentCnt) {
        TeamInfo teamInfo = TeamInfo.toDto(team);
        CardSelectionInfo selectionInfo;
        if(cardOwner != null){
            selectionInfo = CardSelectionInfo.toDto(entity, BriefMemberInfo.toDto(cardOwner));
        }else {
            selectionInfo = null;
        }

        return CardResponse.builder()
                .cardId(entity.getId())
                .writerInfo(BriefMemberInfo.toDto(entity.getWriter()))
                .projTeamInfo(teamInfo)
                .title(entity.getTitle())
                .content(entity.getContent())
                .teammateCnt(entity.getTeammateCnt())
                .teammates(entity.getTeammates())
                .createDate(entity.getCreateDate())
                .lastUpdateDate(entity.getLastUpdateDate())
                .viewCnt(entity.getViewCnt())
                .likeCnt(likeCnt)
                .likeYn(likeYn)
                .commentCnt(commentCnt)
                .selectionInfo(selectionInfo)
                .build();
    }

}
