package com.ktds.dsquare.board.card;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ktds.dsquare.board.card.dto.CardRequest;
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
    @JoinColumn(name = "cardWriter")
    private Member cardWriter;

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

    public static Card toEntity(CardRequest dto, Member writer, Team projTeam){
        LocalDateTime now = LocalDateTime.now();
        return Card.builder()
                .cardWriter(writer)
                .projTeam(projTeam)
                .title(dto.getTitle())
                .content(dto.getContent())
                .teammateCnt(dto.getTeammateCnt())
                .teammate(dto.getTeammate())
                .createDate(now)
                .viewCnt(0L)
                .deleteYn(false)
                .build();
    }

    public void selectCard(Member cardOwner, Boolean selectionYn, LocalDateTime selectedDate){
        this.cardOwner = cardOwner;
        this.selectionYn = selectionYn;
        this.selectedDate = selectedDate;
    }

    public void updateCard(Team projTeam, CardRequest dto, LocalDateTime lastUpdateDate){
        this.projTeam = projTeam;
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.teammateCnt = dto.getTeammateCnt();
        this.teammate = dto.getTeammate();
        this.lastUpdateDate = lastUpdateDate;
    }

    public void deleteCard(){
        this.deleteYn = true;
    }

    public void increaseViewCnt() {
        this.viewCnt += 1;
    }
}
