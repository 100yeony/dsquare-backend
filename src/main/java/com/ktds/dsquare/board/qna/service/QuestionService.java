package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;
    public void createQuestion(Long id, Long writerId, Integer cateId,
                               String title, String content, LocalDateTime createDate,
                               LocalDateTime lastUpdateDate, Long viewCnt,
                               Long atcId, Boolean deleteYn){
        Question question = new Question();
        question.setId(id);
        question.setWriterId(writerId);
        question.setCateId(cateId);
        question.setTitle(title);
        question.setContent(content);
        question.setCreateDate(createDate.now());
        question.setLastUpdateDate(lastUpdateDate.now());
        question.setViewCnt(0L);
        question.setAtcId(atcId);
        question.setDeleteYn(false);

        questionRepository.save(question);

    }
}
