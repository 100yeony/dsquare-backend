package com.ktds.dsquare.board.qna.controller;

import com.ktds.dsquare.board.qna.dto.QuestionDto;
import com.ktds.dsquare.board.qna.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    // 질문글 작성
    @PostMapping("/board/questions")
    public ResponseEntity<Void> createQuestion(@RequestBody QuestionDto request){
        questionService.createQuestion(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 질문글 수정
    @PostMapping("/board/questions/{questionId}")
    public ResponseEntity<Void> updateQuestion(@PathVariable Long questionId, @RequestBody QuestionDto request){
        questionService.updateQuestion(questionId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 질문글 삭제
    @DeleteMapping("/board/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long questionId){
        questionService.deleteQuestion(questionId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
