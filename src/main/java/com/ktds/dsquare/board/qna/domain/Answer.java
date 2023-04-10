package com.ktds.dsquare.board.qna.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Answer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(unique = true, nullable = false)
//    private Long questionId;


    @Column(nullable = false)
    private Long writerId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;

    private Long atcId;
    @Column(nullable = false)
    private boolean deleteYn;


    @JsonBackReference //직렬화 X
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qid")
    private Question qid;

//    @ManyToOne
//    private Question question;


}


