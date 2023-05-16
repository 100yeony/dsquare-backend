package com.ktds.dsquare.board.card;

import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.team.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card,Long>, JpaSpecificationExecutor<Card>{

    //전체 조회 관련
    Page<Card>  findByDeleteYnAndSelectionYnOrderByCreateDateDesc(Boolean deleteYn, Boolean selection, Pageable pageable);

    //상세 조회 관련
    Optional<Card> findByDeleteYnAndId(Boolean deleteYn, Long cardId);

    //검색 관련
    Page<Card> findByDeleteYnAndSelectionYnAndProjTeamOrderByCreateDateDesc(Boolean deleteYn, Boolean selection, Team projTeamId, Pageable pageable);

    //마이페이지 관련
    Page<Card> findByDeleteYnAndWriter(Boolean deleteYn, Member writer, Pageable pageable);

}
