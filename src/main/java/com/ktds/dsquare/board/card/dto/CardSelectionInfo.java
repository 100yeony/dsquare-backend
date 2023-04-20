package com.ktds.dsquare.board.card.dto;

import com.ktds.dsquare.board.card.Card;
import com.ktds.dsquare.member.dto.response.MemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardSelectionInfo {

    private Boolean selectionYn;
    private MemberInfo cardOwner;
    private LocalDateTime selectedDate;

    public static CardSelectionInfo toDto(Card entity, MemberInfo cardOwner){
        return CardSelectionInfo.builder()
                .selectionYn(entity.getSelectionYn())
                .cardOwner(cardOwner)
                .selectedDate(entity.getSelectedDate())
                .build();
    }
}
