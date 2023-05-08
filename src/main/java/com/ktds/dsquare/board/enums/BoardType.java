package com.ktds.dsquare.board.enums;

public enum BoardType {
    QUESTION(Constant.QUESTION),
    ANSWER(Constant.ANSWER),
    CARD(Constant.CARD),
    TALK(Constant.TALK),
    CARROT(Constant.CARROT)
    ;


    public final String name;

    BoardType(String name) {
        this.name = name;
    }

    public static BoardType findBoardType(String boardTypeName) {
        for (BoardType boardType : BoardType.values()) {
            if (boardType.name.equals(boardTypeName))
                return boardType;
        }
        throw new RuntimeException("BoardType Not Found");
    }

    public static class Constant {
        public static final String QUESTION = "question";
        public static final String ANSWER = "answer";
        public static final String CARD = "card";
        public static final String TALK = "talk";
        public static final String CARROT = "carrot";
    }

}
