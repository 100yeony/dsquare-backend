package com.ktds.dsquare.board.qna.repository;

import com.ktds.dsquare.board.qna.domain.Category;
import com.ktds.dsquare.board.qna.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {

    List<Question> findByWriterId(Long writerId);
    List<Question> findByCategory(Category category);
    List<Question> findByTitleContainingOrContentContaining(String title, String content);

    //질문글 전체조회 업무/비업무 구분 - cid=2일때만 비업무
    List<Question> findByDeleteYnAndCategoryOrderByCreateDateDesc(Boolean deleteYn, Category category);
    List<Question> findByDeleteYnAndCategoryNotOrderByCreateDateDesc(Boolean deleteYn, Category category);

    Question findById(Question id);
    Question findQuestionById(Long id);

}
