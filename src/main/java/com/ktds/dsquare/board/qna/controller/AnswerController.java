package com.ktds.dsquare.board.qna.controller;


import com.ktds.dsquare.board.qna.dto.AnswerDto;
import com.ktds.dsquare.board.qna.service.AnswerService;

import com.ktds.dsquare.board.qna.dto.AnswerRequest;
import com.ktds.dsquare.board.qna.repository.AnswerRepository;
import com.ktds.dsquare.board.qna.service.AnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    //create - 답변글 작성
    @PostMapping("/board/questions/{qid}/answer")
    public ResponseEntity<Void> createAnswer(@PathVariable("qid") Long qid, @RequestBody AnswerDto request){
        answerService.createAnswer(qid, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 답변글 수정
    @PostMapping("/board/questions/{qid}/answers/{answerId}")
    public ResponseEntity<Void> updateAnswer(@PathVariable Long qid, @PathVariable Long answerId,
                                             @RequestBody AnswerDto request) {
        answerService.updateAnswer(answerId, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    // 답변글 삭제
    @DeleteMapping("/board/questions/{questionId}/answers/{answerId}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long questionId, @PathVariable Long answerId){
        answerService.deleteAnswer(answerId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/board/questions/{id}/answers")
    public ResponseEntity<Void> creaetAnswer(@RequestBody AnswerRequest request){
        answerService.createAnswer(request.getId(),
                request.getQuestionId(),
                request.getWriterId(),
                request.getContent(),
                request.getCreateDate(),
                request.getLastUpdateDate(),
                request.getAtcId(),
                request.getDeleteYn());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }





}
