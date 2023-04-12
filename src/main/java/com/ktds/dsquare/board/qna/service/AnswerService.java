package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.AnswerRequest;
import com.ktds.dsquare.board.qna.repository.AnswerRepository;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;


    @Autowired
    private QuestionRepository questionRepository;

    // 답변글 작성
    public void createAnswer(Long qid, AnswerRequest dto) {
        Answer answer = new Answer();
        answer.setId(dto.getId());
        answer.setWriterId(dto.getWriterId());
        answer.setContent(dto.getContent());
        LocalDateTime now = LocalDateTime.now();
        answer.setCreateDate(now);
        answer.setLastUpdateDate(now);
        answer.setAtcId(dto.getAtcId());
        answer.setDeleteYn(false);

        Question question = questionRepository.findById(qid).orElseThrow(() -> new RuntimeException("Question not found"));
        answer.setQid(question);

        answerRepository.save(answer);
    }

    // 답변글 조회
    public List<Answer> getAnswersByQid(Question qid) {
        return answerRepository.findByQidAndDeleteYnOrderByCreateDateAsc(qid, false);
    }

    // 답변글 수정
    public void updateAnswer(Long aid, AnswerRequest request) {
        Answer answer = answerRepository.findById(aid).orElseThrow(() -> new RuntimeException("Answer does not exist"));

        answer.setContent(request.getContent());
        answer.setLastUpdateDate(LocalDateTime.now());
        answer.setAtcId(request.getAtcId());

        answerRepository.save(answer);
    }

    // 답변글 삭제
    public void deleteAnswer(Long aid) {
        Answer answer = answerRepository.findById(aid).orElseThrow(() -> new RuntimeException("Answer does not exist"));
        answer.setDeleteYn(true);
        answer.setLastUpdateDate(LocalDateTime.now());
        answerRepository.save(answer);
    }

}
