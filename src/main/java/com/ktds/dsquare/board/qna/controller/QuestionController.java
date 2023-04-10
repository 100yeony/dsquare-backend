package com.ktds.dsquare.board.qna.controller;

import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.QuestionDto;
import com.ktds.dsquare.board.qna.dto.QuestionRequest;
import com.ktds.dsquare.board.qna.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping("/board/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    //create - 질문글 작성
    @PostMapping("/board/questions")
    public ResponseEntity<Void> createQuestion(@RequestBody QuestionDto request){
        questionService.createQuestion(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //read - 질문글 전체 목록 조회
    @GetMapping("/board/questions")
    public List<Question> getAllQuestions(){
        return questionService.getAllQuestions();
    }

    //read - 질문글&답변글 상세 조회
    @GetMapping("/board/questions/{qid}")
    public Optional<Question> getQnADetail(@PathVariable("qid") Long qid) {
        return questionService.getQuestionDetail(qid);
    }

    // 질문글 수정
    @PostMapping("/board/questions/{qid}")
    public ResponseEntity<Void> updateQuestion(@PathVariable("qid") Long qid, @RequestBody QuestionDto request){
        questionService.updateQuestion(qid, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 질문글 삭제
    @DeleteMapping("/board/questions/{qid}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable("qid") Long qid){
        questionService.deleteQuestion(qid);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //search - Q&A 통합검색(사용자, 제목+내용)
    @GetMapping("/board/search")
    public ResponseEntity<List<Question>> search(@RequestParam String keyword, @RequestParam(required = false) Long writerId){
        if(writerId != null){
            return ResponseEntity.ok(questionService.searchByWriterId(writerId));

        } else {
            return ResponseEntity.ok(questionService.searchByTitleOrContent(keyword));
        }
    }

    @PostMapping("/board/questions")
    public ResponseEntity<Void> createQuestion(@RequestBody QuestionRequest request){
        questionService.createQuestion(request.getId(),
                request.getWriterId(),
                request.getCateId(),
                request.getTitle(),
                request.getContent(),
                request.getCreateDate(),
                request.getLastUpdateDate(),
                request.getViewCnt(),
                request.getAtcId(),
                request.getDeleteYn());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



}
