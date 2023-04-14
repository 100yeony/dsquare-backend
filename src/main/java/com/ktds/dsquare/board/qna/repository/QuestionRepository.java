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

    //질문글 전체조회 업무/비업무 구분 - cid=2일때만 비업무
    List<Question> findByDeleteYnAndCidOrderByCreateDateDesc(Boolean deleteYn, Category cid);
    List<Question> findByDeleteYnAndCidNotOrderByCreateDateDesc(Boolean deleteYn, Category cid);


}
