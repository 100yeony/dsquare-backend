package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.QuestionDto;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;


    public void createQuestion(QuestionDto dto){
        Question question = new Question();
        question.setQid(dto.getQid());
        question.setWriterId(dto.getWriterId());
        question.setCateId(dto.getCateId());
        question.setTitle(dto.getTitle());
        question.setContent(dto.getContent());
        question.setCreateDate(dto.getCreateDate().now());
        question.setLastUpdateDate(dto.getLastUpdateDate().now());
        question.setViewCnt(0L);
        question.setAtcId(dto.getAtcId());
        question.setDeleteYn(false);

        questionRepository.save(question);

    }
}
