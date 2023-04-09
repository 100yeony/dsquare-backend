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

    //create - 답변글 작성
    @PostMapping("/board/questions/{qid}/answers")
    public ResponseEntity<Void> createAnswer(@PathVariable("qid") Long qid, @RequestBody AnswerDto request){
        answerService.createAnswer(qid, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 답변글 수정
    @PostMapping("/board/questions/{qid}/answers/{aid}")
    public ResponseEntity<Void> updateAnswer(@PathVariable Long qid, @PathVariable Long aid,
                                             @RequestBody AnswerDto request) {
        answerService.updateAnswer(aid, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    // 답변글 삭제
    @DeleteMapping("/board/questions/{qid}/answers/{aid}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long qid, @PathVariable Long aid){
        answerService.deleteAnswer(aid);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
