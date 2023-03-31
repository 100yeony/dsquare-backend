package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.AnswerDto;
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

    // 질문글  작성
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

    // 질문글 수정
    public void updateQuestion(Long questionId, QuestionDto request) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new RuntimeException("Update Fail"));

        question.setTitle(request.getTitle());
        question.setContent(request.getContent());
        question.setLastUpdateDate(LocalDateTime.now());
        question.setAtcId(request.getAtcId());

        questionRepository.save(question);
    }

}
