package com.ktds.dsquare.board.qna.controller;

import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.BriefQuestionResponse;
import com.ktds.dsquare.board.qna.dto.QuestionRequest;
import com.ktds.dsquare.board.qna.dto.QuestionResponse;
import com.ktds.dsquare.board.qna.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    //create - 질문글 작성
    @PostMapping("/board/questions")
    public ResponseEntity<Void> createQuestion(@RequestBody QuestionRequest request){
        questionService.createQuestion(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //read - 질문글 전체 목록 조회
    @GetMapping("/board/questions")
    public ResponseEntity<List<BriefQuestionResponse>> getAllQuestions(@RequestParam Boolean workYn){
        return new ResponseEntity<>(questionService.getAllQuestions(workYn), HttpStatus.OK);
    }

    //read - 질문글 상세 조회
    @GetMapping("/board/questions/{qid}")
    public ResponseEntity<QuestionResponse> getQnADetail(@PathVariable("qid") Long qid) {
        return new ResponseEntity(questionService.getQuestionDetail(qid), HttpStatus.OK);
    }

    // 질문글 수정
    @PostMapping("/board/questions/{qid}")
    public ResponseEntity<Void> updateQuestion(@PathVariable("qid") Long qid, @RequestBody QuestionRequest request){
        questionService.updateQuestion(qid, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 질문글 삭제
    @DeleteMapping("/board/questions/{qid}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable("qid") Long qid){
        questionService.deleteQuestion(qid);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //search - Q&A 검색(카테고리, 제목+내용, 사용자(이름))
    @GetMapping("/board/questions/search")
    @ResponseBody
    public ResponseEntity<List<Question>> search(@RequestParam(required = false) Integer cid, @RequestParam String key, @RequestParam String value) {
        if (cid != null) {
            List<Question> questions = questionService.searchByCid(cid);
            return ResponseEntity.ok(questions);
        }
        if (key.contentEquals("titleAndContent")) {
            List<Question> questions = questionService.searchByTitleOrContent(value);
            return ResponseEntity.ok(questions);

        } else if (key.contentEquals("member")) {
            List<Question> questions = questionService.searchByName(value);
            return ResponseEntity.ok(questions);
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

}







