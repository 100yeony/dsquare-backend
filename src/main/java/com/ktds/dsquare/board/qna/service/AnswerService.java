package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.AnswerDto;
import com.ktds.dsquare.board.qna.repository.AnswerRepository;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public void createAnswer(Long qid, AnswerDto dto) {
        Answer answer = new Answer();
        answer.setId(dto.getId());
        answer.setWriterId(dto.getWriterId());
        answer.setContent(dto.getContent());
        answer.setCreateDate(dto.getCreateDate().now());
        answer.setLastUpdateDate(dto.getLastUpdateDate().now());
        answer.setAtcId(dto.getAtcId());
        answer.setDeleteYn(false);


        Question question = questionRepository.findById(qid).orElseThrow(() -> new RuntimeException("Question not found"));
        answer.setQuestion(question);

        answerRepository.save(answer);
    }
}
