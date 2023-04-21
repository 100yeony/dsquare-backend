package com.ktds.dsquare.board.qna.repository;

import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestionAndDeleteYn(Question qid, Boolean deleteYn);
    List<Answer> findByQuestionAndDeleteYnOrderByCreateDateAsc(Question qid, Boolean deleteYn);

}