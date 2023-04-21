package com.ktds.dsquare.board.enums;

import lombok.Getter;

@Getter
public enum BoardType {
    QUESTION(0L),
    ANSWER(1L),
    CARD(2L),
    TALK(3L),
    CARROT(4L);

    private final Long value;

    BoardType(Long value) {
        this.value = value;
    }
}
