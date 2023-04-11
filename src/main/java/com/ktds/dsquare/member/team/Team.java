package com.ktds.dsquare.member.team;

import lombok.*;

import javax.persistence.*;
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

}
