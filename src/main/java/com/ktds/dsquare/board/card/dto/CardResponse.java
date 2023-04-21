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
    private String teammate;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private Long viewCnt;
    private CardSelectionInfo selectionInfo;

//    public static CardResponse toDto(Card entity, MemberInfo writerInfo, TeamInfo teamInfo, CardSelectionInfo selectionInfo) {
//        return CardResponse.builder()
//                .cardId(entity.getCardId())
//                .writerInfo(writerInfo)
//                .projTeamInfo(teamInfo)
//                .title(entity.getTitle())
//                .content(entity.getContent())
//                .teammate(entity.getTeammate())
//                .createDate(entity.getCreateDate())
//                .lastUpdateDate(entity.getLastUpdateDate())
//                .viewCnt(entity.getViewCnt())
//                .selectionInfo(selectionInfo)
//                .build();
//    }

    public static CardResponse toDto(Card entity, Member writer, Team team, Member cardOwner) {
        MemberInfo writerInfo = MemberInfo.toDto(writer);
        TeamInfo teamInfo = TeamInfo.toDto(team);
        Member owner = cardOwner;
        CardSelectionInfo selectionInfo;
        if(owner != null){
            selectionInfo = CardSelectionInfo.toDto(entity, MemberInfo.toDto(owner));
        }else {
            selectionInfo = null;
        }

        return CardResponse.builder()
                .cardId(entity.getCardId())
                .writerInfo(writerInfo)
                .projTeamInfo(teamInfo)
                .title(entity.getTitle())
                .content(entity.getContent())
                .teammate(entity.getTeammate())
                .createDate(entity.getCreateDate())
                .lastUpdateDate(entity.getLastUpdateDate())
                .viewCnt(entity.getViewCnt())
                .selectionInfo(selectionInfo)
                .build();
    }


/*    Member member = card.getCardWriter();
    MemberInfo writer = MemberInfo.toDto(member);

    Team team = card.getProjTeam();
    TeamInfo teamInfo = TeamInfo.toDto(team);

    Member owner = card.getCardOwner();
    CardSelectionInfo selectionInfo;
        if(owner != null){
        MemberInfo cardOwner = MemberInfo.toDto(owner);
        selectionInfo = CardSelectionInfo.toDto(card, cardOwner);
    }else{
        selectionInfo = null;
    }*/


}