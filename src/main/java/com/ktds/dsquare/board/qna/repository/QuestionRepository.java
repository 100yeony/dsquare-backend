package com.ktds.dsquare.board.qna.repository;

import com.ktds.dsquare.board.qna.domain.Category;
import com.ktds.dsquare.board.qna.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {

    List<Question> findByWriterId(Long writerId);
    List<Question> findByCid(Category cid);
    List<Question> findByTitleContainingOrContentContaining(String title, String content);
    List<Question> findByDeleteYnOrderByQidDesc(Boolean deleteYn);
    List<Question> findByDeleteYn(Boolean deleteYn);
    List<Question> findByDeleteYnOrderByCreateDateDesc(Boolean deleteYn);

}
