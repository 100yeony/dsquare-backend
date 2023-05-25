package com.ktds.dsquare.board.card.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ktds.dsquare.board.card.Card;
import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardUpdateResponse {

    private long cardId;
    private BriefMemberInfo writerInfo;
    private List<String> projTeamHierarchy;
    private String title;
    private String content;

    private int teammateCnt;
    private List<String> teammates;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+9")
    private LocalDateTime createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+9")
    private LocalDateTime lastUpdateDate;


    public static CardUpdateResponse toDto(Card entity) {
        return CardUpdateResponse.builder()
                .cardId(entity.getId())
                .writerInfo(BriefMemberInfo.toDto(entity.getWriter()))
                .projTeamHierarchy(entity.getProjTeam().getTeamHierarchy())
                .title(entity.getTitle())
                .content(entity.getContent())
                .teammateCnt(entity.getTeammateCnt())
                .teammates(entity.getTeammates())
                .createDate(entity.getCreateDate())
                .lastUpdateDate(entity.getLastUpdateDate())
                .build();
    }

}
