package com.ktds.dsquare.board.qna.repository;

import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.domain.QuestionTag;
import com.ktds.dsquare.board.qna.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionTagRepository extends JpaRepository<QuestionTag,Long> {
    void deleteByQuestionAndTag(Question question, Tag tag);

}
