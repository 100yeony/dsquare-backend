package com.ktds.dsquare.board.qna.controller;

import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.QuestionRequest;
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
