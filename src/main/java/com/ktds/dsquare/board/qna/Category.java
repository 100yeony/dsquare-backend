package com.ktds.dsquare.board.qna;

import javax.persistence.Column;
import javax.persistence.Id;

public class Category {
    @Id
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false, length = 20)  // unique = true 넣어야할까?
    private String name;

    @Column(length = 2)
    private String upId;

    @Column(nullable = false)
    private Integer depth;

    @Column(nullable = false)
    private Boolean workYn;     //  기본값은 true

}
