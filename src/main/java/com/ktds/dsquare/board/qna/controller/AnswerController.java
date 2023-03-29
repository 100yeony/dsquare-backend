package com.ktds.dsquare.board.qna.controller;

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
