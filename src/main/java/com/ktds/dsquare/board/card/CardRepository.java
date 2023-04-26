package com.ktds.dsquare.board.card;

import com.ktds.dsquare.member.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card,Long>, JpaSpecificationExecutor<Card>{

    //전체 조회 관련
    List<Card>  findByDeleteYnOrderByCreateDateDesc(Boolean deleteYn);

    //상세 조회 관련
    Card findByDeleteYnAndId(Boolean deleteYn, Long cardId);

    //검색 관련
    List<Card> findByDeleteYnAndProjTeamOrderByCreateDateDesc(Boolean deleteYn, Team projTeamId);

    //이달의 카드 조회 관련
    @Query(value = "SELECT DISTINCT ON (extract(month from c.selected_date)) " +
            "c.*, count(*) AS like_cnt " +
            "FROM comm_card c " +
            "JOIN like_post l ON c.id = l.post_id AND l.board_Type = 'CARD' " +
            "WHERE c.selection_yn IS true " +
            "AND c.delete_yn IS false " +
            "GROUP BY c.id, " +
            "extract(month from c.selected_date) " +
            "ORDER BY extract(month from c.selected_date), like_cnt DESC", nativeQuery = true)
    List<Card> findSelectedCard();


}
