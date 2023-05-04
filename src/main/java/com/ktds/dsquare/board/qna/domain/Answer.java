package com.ktds.dsquare.board.qna.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ktds.dsquare.board.qna.dto.AnswerRequest;
import com.ktds.dsquare.member.Member;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private Member writer;

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
    @JoinColumn(name = "question")
    private Question question;

    private Long likeCnt;

    public static Answer toEntity(AnswerRequest dto, Member writer, Question question){
        LocalDateTime now = LocalDateTime.now();
        return Answer.builder()
                .writer(writer)
                .content(dto.getContent())
                .createDate(now)
                .atcId(dto.getAtcId())
                .deleteYn(false)
                .question(question)
                .likeCnt(0L)
                .build();
    }

    public void updateAnswer(String content, Long atcId){
        LocalDateTime now = LocalDateTime.now();
        this.content = content;
        this.lastUpdateDate = now;
        this.atcId = atcId;
    }

    public void deleteAnswer(){
        this.deleteYn = true;
    }

    public void like() {
        this.likeCnt += 1;
    }

    public void cancleLike(){ this.likeCnt -= 1; }

}


