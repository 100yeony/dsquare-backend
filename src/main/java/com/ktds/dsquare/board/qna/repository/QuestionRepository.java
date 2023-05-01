package com.ktds.dsquare.board.qna.repository;

import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.member.Member;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long>, JpaSpecificationExecutor<Question> {

    Question findByDeleteYnAndQid(Boolean deleteYn, Long qid);

    //검색 관련
    List<Question> findAll(Specification<Question> filter, Sort sort);

    //마이페이지 관련
    List<Question> findByDeleteYnAndWriter(Boolean deleteYn, Member writer);
}

