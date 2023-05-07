package com.ktds.dsquare.board.qna.repository;

import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long>, JpaSpecificationExecutor<Question> {

    Question findByDeleteYnAndQid(Boolean deleteYn, Long qid);

    //전체조회 & 검색 관련
    Page<Question> findAll(Specification<Question> filter, Pageable pageable);

    //마이페이지 관련
    List<Question> findByDeleteYnAndWriter(Boolean deleteYn, Member writer);

    //대시보드 관련
    @Query(value = "SELECT q.writer, count(*) " +
            "FROM question q " +
            "GROUP BY q.writer " +
            "ORDER BY count(*) DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Object[]> findBestUser(@Param("limit") int limit);

}

