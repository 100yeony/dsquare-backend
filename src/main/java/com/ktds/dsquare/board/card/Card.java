package com.ktds.dsquare.board.card;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ktds.dsquare.board.card.dto.CardRequest;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.team.Team;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Table(name = "COMM_CARD")
public class Card {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

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
        return Card.builder()
                .cardWriter(writer)
                .projTeam(projTeam)
                .title(dto.getTitle())
                .content(dto.getContent())
                .teammate(dto.getTeammate())
                .build();
    }

    public void toDefault(LocalDateTime createDate, LocalDateTime lastUpdateDate,
                            Long viewCnt, Boolean deleteYn){
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.viewCnt = viewCnt;
        this.deleteYn = deleteYn;
    }

    public void selectCard(Member cardOwner, Boolean selectionYn, LocalDateTime selectedDate){
        this.cardOwner = cardOwner;
        this.selectionYn = selectionYn;
        this.selectedDate = selectedDate;
    }

    public void updateCard(Team projTeam, String title, String content,
                           String teammate, LocalDateTime lastUpdateDate){
        this.projTeam = projTeam;
        this.title = title;
        this.content = content;
        this.teammate = teammate;
        this.lastUpdateDate = lastUpdateDate;
    }

    public void deleteCard(){
        this.deleteYn = true;
    }

    public void increaseViewCnt() {
        this.viewCnt += 1;
    }
}
