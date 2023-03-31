package com.ktds.dsquare.board.qna.controller;

import com.ktds.dsquare.board.qna.dto.QuestionDto;
import com.ktds.dsquare.board.qna.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/board/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    @PostMapping("/board/questions")
    public ResponseEntity<Void> createQuestion(@RequestBody QuestionDto request){
        questionService.createQuestion(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
