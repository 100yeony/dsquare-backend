package com.ktds.dsquare.board.card;

import com.ktds.dsquare.member.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card,Long>, JpaSpecificationExecutor<Card>{

    //조회 관련
    List<Card>  findByDeleteYnOrderByCreateDateDesc(Boolean deleteYn);

    //검색 관련
    List<Card> findByDeleteYnAndProjTeamOrderByCreateDateDesc(Boolean deleteYn, Team projTeamId);
}
