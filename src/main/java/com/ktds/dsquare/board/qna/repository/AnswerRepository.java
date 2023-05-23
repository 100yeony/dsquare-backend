package com.ktds.dsquare.board.qna.repository;

import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long>, JpaSpecificationExecutor<Answer> {
    List<Answer> findByQuestionAndDeleteYn(Question qid, Boolean deleteYn);
    List<Answer> findByQuestionAndDeleteYnOrderByCreateDateAsc(Question qid, Boolean deleteYn);

    Optional<Answer> findByDeleteYnAndId(Boolean deleteYn, Long aid);

    //마이페이지 관련
    Page<Answer> findByDeleteYnAndWriter(Boolean deleteYn, Member writer, Pageable pageable);

    long countByQuestion(Question question);

    boolean existsByWriter(Member writer);

    //대시보드 관련
    @Query(value = "SELECT a.writer, count(*) " +
            "FROM answer a " +
            "GROUP BY a.writer " +
            "ORDER BY count(*) DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Object[]> findBestUser(@Param("limit") int limit);

}
