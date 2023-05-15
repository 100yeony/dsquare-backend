package com.ktds.dsquare.board.card;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ktds.dsquare.board.Post;
import com.ktds.dsquare.board.card.dto.CardRegisterRequest;
import com.ktds.dsquare.board.card.dto.CardUpdateRequest;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.team.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
@DynamicUpdate
@Table(name = "COMM_CARD")
public class Card extends Post {

    @JsonBackReference //직렬화 X
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private Member writer;

    @JsonBackReference //직렬화 X
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proj_team")
    private Team projTeam;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 300)
    private String content;

    private Integer teammateCnt;

    private String teammates;

    @Column(nullable = false)
    private LocalDateTime createDate;

    private LocalDateTime lastUpdateDate;

    @Column(nullable = false)
    private Long viewCnt;

    private Boolean selectionYn;

    @JsonBackReference //직렬화 X
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_owner")
    private Member cardOwner;

    private LocalDateTime selectedDate;

    @Column(nullable = false)
    private Boolean deleteYn;

    private Long likeCnt;

    public static Card toEntity(CardRegisterRequest dto, Member writer, Team projTeam){
        String teammates = dto.getTeammates().toString();
        LocalDateTime now = LocalDateTime.now();
        return Card.builder()
                .writer(writer)
                .projTeam(projTeam)
                .title(dto.getTitle())
                .content(dto.getContent())
                .teammateCnt(dto.getTeammateCnt())
                .teammates(teammates)
                .createDate(now)
                .viewCnt(0L)
                .deleteYn(false)
                .likeCnt(0L)
                .selectionYn(false)
                .build();
    }

    public void selectCard(Member cardOwner, Boolean selectionYn){
        LocalDateTime now = LocalDateTime.now();
        this.cardOwner = cardOwner;
        this.selectionYn = selectionYn;
        this.selectedDate = now;
    }

    public void updateCard(Team projTeam, CardUpdateRequest dto){
        String teammates = dto.getTeammates().toString();
        LocalDateTime now = LocalDateTime.now();
        this.projTeam = projTeam;
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.teammateCnt = dto.getTeammateCnt();
        this.teammates = teammates;
        this.lastUpdateDate = now;
    }

    public void deleteCard(){
        this.deleteYn = true;
    }

    public void increaseViewCnt() {
        this.viewCnt += 1;
    }

    public void like() { this.likeCnt += 1; }

    public void cancleLike(){ this.likeCnt -= 1; }
}
