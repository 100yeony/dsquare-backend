package com.ktds.dsquare.board.card.dto.response;

import com.ktds.dsquare.board.card.Card;
import com.ktds.dsquare.board.card.dto.CardSelectionInfo;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardChooseResponse {

    private long id;
    private CardSelectionInfo selectionInfo;


    public static CardChooseResponse toDto(Card entity) {
        return CardChooseResponse.builder()
                .id(entity.getId())
                .selectionInfo(CardSelectionInfo.toDto(entity))
                .build();
    }

}
