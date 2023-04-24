package com.ktds.dsquare.board.card.dto;

import com.ktds.dsquare.board.card.Card;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.dto.response.MemberInfo;
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
    private MemberInfo writerInfo;
    private TeamInfo projTeamInfo;
    private String title;
    private String content;
    private Integer teammateCnt;
    private String teammate;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private Long viewCnt;
    private Integer likeCnt;
    private Boolean likeYn;
    private CardSelectionInfo selectionInfo;

    public static CardResponse toDto(Card entity, Member writer, Team team, Member cardOwner, Integer likeCnt, Boolean likeYn) {
        MemberInfo writerInfo = MemberInfo.toDto(writer);
        TeamInfo teamInfo = TeamInfo.toDto(team);
        CardSelectionInfo selectionInfo;
        if(cardOwner != null){
            selectionInfo = CardSelectionInfo.toDto(entity, MemberInfo.toDto(cardOwner));
        }else {
            selectionInfo = null;
        }

        return CardResponse.builder()
                .cardId(entity.getId())
                .writerInfo(writerInfo)
                .projTeamInfo(teamInfo)
                .title(entity.getTitle())
                .content(entity.getContent())
                .teammateCnt(entity.getTeammateCnt())
                .teammate(entity.getTeammate())
                .createDate(entity.getCreateDate())
                .lastUpdateDate(entity.getLastUpdateDate())
                .viewCnt(entity.getViewCnt())
                .likeCnt(likeCnt)
                .likeYn(likeYn)
                .selectionInfo(selectionInfo)
                .build();
    }

}
