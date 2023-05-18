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
@NoArgsConstructor
@AllArgsConstructor
public class BriefCardResponse {

    private Long cardId;
    private BriefMemberInfo writerInfo;
    private TeamInfo projTeamInfo;
    private String title;
    private String content;
    private Integer teammateCnt;
    private String teammates;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+9")
    private LocalDateTime createDate;
    private Long viewCnt;
    private Long likeCnt;
    private Boolean likeYn;
    private Long commentCnt;
    private CardSelectionInfo selectionInfo;

    public static BriefCardResponse toDto(Card entity, TeamInfo teamInfo, CardSelectionInfo selectionInfo,
                                          Long likeCnt, Boolean likeYn, Long commentCnt) {
        return BriefCardResponse.builder()
                .cardId(entity.getId())
                .writerInfo(BriefMemberInfo.toDto(entity.getWriter()))
                .projTeamInfo(teamInfo)
                .title(entity.getTitle())
                .content(entity.getContent())
                .teammateCnt(entity.getTeammateCnt())
                .teammates(entity.getTeammates())
                .createDate(entity.getCreateDate())
                .viewCnt(entity.getViewCnt())
                .likeCnt(likeCnt)
                .likeYn(likeYn)
                .commentCnt(commentCnt)
                .selectionInfo(selectionInfo)
                .build();
    }
}
