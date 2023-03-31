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
import java.util.List;
import java.util.Optional;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    //create - 답변글 작성
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

//    //read - 답변글 전체 조회(답변글은 상세조회 x)
//    public List<Answer> getAnswers(Long qid){
//        List<Answer> answer = this.answerRepository.findAllByQid(qid);
//        return answer;
//    }

}
