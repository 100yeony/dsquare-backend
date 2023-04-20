package com.ktds.dsquare.member.team;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ktds.dsquare.board.card.Card;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Team {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tid;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team upperDepartment;
    @OneToMany(mappedBy = "upperDepartment")
    private List<Team> childDepartments;

    @JsonManagedReference //직렬화
    @OneToMany(mappedBy = "projTeam")
    private List<Card> cardList;


    public List<String> getTeamHierarchy() {
        Deque<String> nameQueue = new ArrayDeque<>();
        for (Team team = this; team != null; team = team.getUpperDepartment())
            nameQueue.offerFirst(team.getName());

        return new ArrayList<>(nameQueue);
    }

}
