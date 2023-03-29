package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    public void createAnswer(Long id, Long questionId, Long writerId,
                             String content, LocalDateTime createDate,
                             LocalDateTime lastUpdateDate,
                             Long atcId, Boolean deleteYn){
        Answer answer = new Answer();
        answer.setId(id);
        answer.setQuestionId(questionId);
        answer.setWriterId(writerId);
        answer.setContent(content);
        answer.setCreateDate(createDate.now());
        answer.setLastUpdateDate(lastUpdateDate.now());
        answer.setAtcId(atcId);
        answer.setDeleteYn(false);

        answerRepository.save(answer);
    }
}
