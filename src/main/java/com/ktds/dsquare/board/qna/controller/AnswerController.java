package com.ktds.dsquare.board.qna.controller;

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

    // 답변글 작성
    @PostMapping("/board/questions/{qid}/answers")
    public ResponseEntity<Void> createAnswer(@PathVariable Long qid, @RequestBody AnswerDto request) {
        answerService.createAnswer(qid, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 답변글 수정
    @PostMapping("/board/questions/{qid}/answers/{answerId}")
    public ResponseEntity<Void> updateAnswer(@PathVariable Long qid, @PathVariable Long answerId,
                                             @RequestBody AnswerDto request) {
        answerService.updateAnswer(qid, answerId, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}
