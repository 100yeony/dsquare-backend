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
public class BriefCardResponse {

    private Long cardId;
    private MemberInfo writerInfo;
    private TeamInfo projTeamInfo;
    private String title;
    private String content;
    private Integer teammateCnt;
    private String teammate;
    private LocalDateTime createDate;
    private Long viewCnt;
    private Long commentCnt;
    private CardSelectionInfo selectionInfo;

    public static BriefCardResponse toDto(Card entity, MemberInfo writerInfo, TeamInfo teamInfo, CardSelectionInfo selectionInfo, Long commentCnt) {
        return BriefCardResponse.builder()
                .cardId(entity.getId())
                .writerInfo(writerInfo)
                .projTeamInfo(teamInfo)
                .title(entity.getTitle())
                .content(entity.getContent())
                .teammateCnt(entity.getTeammateCnt())
                .teammate(entity.getTeammate())
                .createDate(entity.getCreateDate())
                .viewCnt(entity.getViewCnt())
                .selectionInfo(selectionInfo)
                .commentCnt(commentCnt)
                .build();
    }
}
