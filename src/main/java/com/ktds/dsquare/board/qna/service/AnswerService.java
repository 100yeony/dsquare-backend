package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.AnswerDto;
import com.ktds.dsquare.board.qna.repository.AnswerRepository;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;

import com.ktds.dsquare.board.qna.repository.AnswerRepository;
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

    // 답변글 작성
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
        answer.setQid(question);

        answerRepository.save(answer);
    }

    // 답변글 수정
    public void updateAnswer(Long answerId, AnswerDto request) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new RuntimeException("Update Fail"));

        answer.setContent(request.getContent());
        answer.setLastUpdateDate(LocalDateTime.now());
        answer.setAtcId(request.getAtcId());

        answerRepository.save(answer);
    }

    // 답변글 삭제
    public void deleteAnswer(Long answerId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new RuntimeException("Delete Fail"));
        answer.setDeleteYn(true);
        answer.setLastUpdateDate(LocalDateTime.now());
        answerRepository.save(answer);
    }


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
