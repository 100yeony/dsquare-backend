package com.ktds.dsquare.board.card;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ktds.dsquare.board.card.dto.CardRegisterRequest;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.team.Team;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Table(name = "COMM_CARD")
public class Card {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference //직렬화 X
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private Member writer;

    @JsonBackReference //직렬화 X
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projTeam")
    private Team projTeam;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private Integer teammateCnt;

    private String teammate;

    @Column(nullable = false)
    private LocalDateTime createDate;

    private LocalDateTime lastUpdateDate;

    @Column(nullable = false)
    private Long viewCnt;

    private Boolean selectionYn;

    @JsonBackReference //직렬화 X
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cardOwner")
    private Member cardOwner;

    private LocalDateTime selectedDate;

    @Column(nullable = false)
    private Boolean deleteYn;

    public static Card toEntity(CardRegisterRequest dto, Member writer, Team projTeam){
        String teammate = dto.getTeammate().toString();
        LocalDateTime now = LocalDateTime.now();
        return Card.builder()
                .writer(writer)
                .projTeam(projTeam)
                .title(dto.getTitle())
                .content(dto.getContent())
                .teammateCnt(dto.getTeammateCnt())
                .teammate(teammate)
                .createDate(now)
                .viewCnt(0L)
                .deleteYn(false)
                .build();
    }

    public void selectCard(Member cardOwner, Boolean selectionYn){
        LocalDateTime now = LocalDateTime.now();
        this.cardOwner = cardOwner;
        this.selectionYn = selectionYn;
        this.selectedDate = now;
    }

    public void updateCard(Team projTeam, CardRegisterRequest dto){
        String teammate = dto.getTeammate().toString();
        LocalDateTime now = LocalDateTime.now();
        this.projTeam = projTeam;
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.teammateCnt = dto.getTeammateCnt();
        this.teammate = teammate;
        this.lastUpdateDate = now;
    }

    public void deleteCard(){
        this.deleteYn = true;
    }

    public void increaseViewCnt() {
        this.viewCnt += 1;
    }
}
