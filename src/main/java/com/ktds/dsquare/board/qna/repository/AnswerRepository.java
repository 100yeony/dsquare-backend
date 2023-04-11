package com.ktds.dsquare.board.qna.repository;

import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
//    List<Answer> findAllByQid(Long qid);

    List<Answer> findByQidAndDeleteYn(Question qid, Boolean deleteYn);
    List<Answer> findByQidAndDeleteYnOrderByIdDesc(Question qid, Boolean deleteYn);

}
