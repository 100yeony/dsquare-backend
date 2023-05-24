package com.ktds.dsquare.board.card;

import com.ktds.dsquare.board.Post;
import com.ktds.dsquare.board.card.dto.request.CardRegisterRequest;
import com.ktds.dsquare.board.card.dto.request.CardUpdateRequest;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.team.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
@DynamicInsert @DynamicUpdate
@Table(name = "COMM_CARD")
public class Card extends Post {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proj_team")
    private Team projTeam;

    @Column(nullable = false, length = 50)
    private String title;
    @Column(nullable = false, length = 300)
    private String content;

    @Column(nullable = false)
    private Integer teammateCnt;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> teammates;

    @ColumnDefault("now()")
    private LocalDateTime createDate;

    private LocalDateTime lastUpdateDate;

    @ColumnDefault("0")
    private Long viewCnt;

    @ColumnDefault("0")
    private Long likeCnt;

    @ColumnDefault("false")
    private Boolean selectionYn;
    private LocalDateTime selectedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_owner")
    private Member cardOwner;

    @ColumnDefault("false")
    private Boolean deleteYn;


    public static Card toEntity(CardRegisterRequest dto, Member writer, Team projTeam){
        return Card.builder()
                .writer(writer)
                .projTeam(projTeam)
                .title(dto.getTitle())
                .content(dto.getContent())
                .teammateCnt(dto.getTeammateCnt())
                .teammates(dto.getTeammates())
                .createDate(LocalDateTime.now().plusHours(9))
                .build();
    }

    public void selectCard(Member cardOwner, Boolean selectionYn){
        LocalDateTime now = LocalDateTime.now();
        this.cardOwner = cardOwner;
        this.selectionYn = selectionYn;
        this.selectedDate = now.plusHours(9);
    }

    public void updateCard(Team projTeam, CardUpdateRequest dto){
        LocalDateTime now = LocalDateTime.now();
        this.projTeam = projTeam;
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.teammateCnt = dto.getTeammateCnt();
        this.teammates = dto.getTeammates();
        this.lastUpdateDate = now.plusHours(9);
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
