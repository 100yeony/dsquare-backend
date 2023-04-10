package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Question;

import com.ktds.dsquare.board.qna.dto.QuestionDto;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;

import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import net.bytebuddy.asm.Advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;


@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;


    //create - 질문글 작성
    public void createQuestion(QuestionDto dto) {
        Question question = new Question();
        question.setQid(dto.getQid());
        question.setWriterId(dto.getWriterId());
        question.setCateId(dto.getCateId());
        question.setTitle(dto.getTitle());
        question.setContent(dto.getContent());
        LocalDateTime now = LocalDateTime.now();
        question.setCreateDate(now);
        question.setLastUpdateDate(now);
        question.setViewCnt(0L);
        question.setAtcId(dto.getAtcId());
        question.setDeleteYn(false);

        questionRepository.save(question);
    }

    //read - 질문글 전체 조회
    public List<Question> getAllQuestions() {
        // deleteYn = false만 필터링 한 후 qid 기준으로 정렬
        return questionRepository.findByDeleteYnOrderByQidAsc(false);
    }

    //read - 질문글 상세 조회
    public Optional<Question> getQuestionDetail(Long qid) {
        /*Question question = questionRepository.findById(qid)
                .orElseThrow(() -> new RuntimeException("Question not found"));*/
        // 삭제된 답변은 안보여야 함(추후 수정 필요)
        return questionRepository.findById(qid);
    }


    // 질문글 수정
    public void updateQuestion(Long qid, QuestionDto request) {
        Question question = questionRepository.findById(qid).orElseThrow(() -> new RuntimeException("Update Fail"));

        question.setTitle(request.getTitle());
        question.setContent(request.getContent());
        question.setLastUpdateDate(LocalDateTime.now());
        question.setAtcId(request.getAtcId());

        questionRepository.save(question);
    }

    // 질문글 삭제
    public void deleteQuestion(Long qid) {
        Question question = questionRepository.findById(qid).orElseThrow(() -> new RuntimeException("Delete Fail"));

        List<Answer> answerList = question.getAnswerList();
        if(answerList.isEmpty()) {
            question.setDeleteYn(true);
            question.setLastUpdateDate(LocalDateTime.now());
            questionRepository.save(question);
        } else {
            // 답변글이 이미 존재할 때 => HTTP Status로 처리해줘야 함(추후 수정 필요)
            throw new RuntimeException("Delete Fail");
        }
    }


    //search - Q&A 통합 검색(사용자, 제목+내용)
    public List<Question> searchByWriterId(Long writerId) {
        return questionRepository.findByWriterId(writerId);
    }

    public List<Question> searchByTitleOrContent(String keyword) {
        return questionRepository.findByTitleContainingOrContentContaining(keyword, keyword);

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
