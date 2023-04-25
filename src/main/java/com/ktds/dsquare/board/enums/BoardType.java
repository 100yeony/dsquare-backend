package com.ktds.dsquare.board.enums;

public enum BoardType {
    QUESTION,
    ANSWER,
    CARD,
    TALK,
    CARROT;

    public static BoardType findBoardType(String boardTypeName) {
        switch(boardTypeName) {
            case "question":
                return BoardType.QUESTION;
            case "answer":
                return BoardType.ANSWER;
            case "card":
                return BoardType.CARD;
            case "talk":
                return BoardType.TALK;
            case "carrot":
                return BoardType.CARROT;
            default:
                throw new RuntimeException("BoardType Not Found");
        }
    }
}
