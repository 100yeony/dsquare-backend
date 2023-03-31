package com.ktds.dsquare.board.qna.controller;

import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.AnswerDto;
import com.ktds.dsquare.board.qna.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @PostMapping("/board/questions/{qid}/answer")
    public ResponseEntity<Void> createAnswer(@PathVariable Long qid, @RequestBody AnswerDto request){
//        System.out.println(qid);
        answerService.createAnswer(qid, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }




}
