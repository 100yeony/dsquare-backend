package com.ktds.dsquare.board.card.dto.response;

import com.ktds.dsquare.board.card.Card;
import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardRegisterResponse {

    private long id;

    private BriefMemberInfo writer;
    private List<String> projTeamHierarchy;

    private String title;
    private String content;

    private int teammateCnt;
    private List<String> teammates;


    public static CardRegisterResponse toDto(Card entity) {
        return CardRegisterResponse.builder()
                .id(entity.getId())
                .writer(BriefMemberInfo.toDto(entity.getWriter()))
                .title(entity.getTitle())
                .content(entity.getContent())
                .projTeamHierarchy(entity.getProjTeam().getTeamHierarchy())
                .teammateCnt(entity.getTeammateCnt())
                .teammates(entity.getTeammates())
                .build();
    }

}
