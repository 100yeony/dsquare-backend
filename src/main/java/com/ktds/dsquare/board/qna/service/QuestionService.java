package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.QuestionDto;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    //create - 질문글 작성
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


    //read - 질문글 전체 조회
    public List<Question> getAllQuestions(){
        return questionRepository.findAll();
    }

    //read - 질문글 상세 조회
    public Optional<Question> getQuestionDetail(Long qid){
        /*Question question = questionRepository.findById(qid)
                .orElseThrow(() -> new RuntimeException("Question not found"));*/

        return questionRepository.findById(qid);
    }
}
