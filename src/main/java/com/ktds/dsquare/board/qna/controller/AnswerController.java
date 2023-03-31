package com.ktds.dsquare.board.qna.controller;

import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.AnswerDto;
import com.ktds.dsquare.board.qna.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    //create - 답변글 작성
    @PostMapping("/board/questions/{qid}/answer")
    public ResponseEntity<Void> createAnswer(@PathVariable("qid") Long qid, @RequestBody AnswerDto request){
//        System.out.println(qid);
        answerService.createAnswer(qid, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //read - 답변글 전체 목록 조회(답변글은 상세조회 x)
//    @GetMapping("/board/questions/{qid}/answers")
//    public List<Answer> getAnswers(@PathVariable("qid") Long qid){
//        return answerService.getAnswers(qid);
//    }



}
