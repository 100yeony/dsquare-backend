package com.ktds.dsquare.board.enums;

public enum BoardType {
    QUESTION,
    ANSWER,
    CARD,
    TALK,
    CARROT;

    public static BoardType findBoardType(Long boardTypeId) {
        switch(boardTypeId.intValue()) {
            case 0:
                return BoardType.QUESTION;
            case 1:
                return BoardType.ANSWER;
            case 2:
                return BoardType.CARD;
            case 3:
                return BoardType.TALK;
            case 4:
                return BoardType.CARROT;
            default:
                throw new RuntimeException("BoardType Not Found");
        }
    }
}
