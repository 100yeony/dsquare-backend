package com.ktds.dsquare.board.qna.controller;


import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.AnswerRequest;
import com.ktds.dsquare.board.qna.service.AnswerService;
import com.ktds.dsquare.board.qna.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;
    private final QuestionService questionService;

    //create - 답변글 작성
    @PostMapping("/board/questions/{qid}/answers")
    public ResponseEntity<Void> createAnswer(@PathVariable("qid") Long qid, @RequestBody AnswerRequest request){
        answerService.createAnswer(qid, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // qid와 연결된 답변 모두 조회
    @GetMapping("/board/questions/{qid}/answers")
    public List<Answer> getAnswersByQid(@PathVariable Long qid) {
        Question question = questionService.getQuestionDetail(qid);
        return answerService.getAnswersByQid(question);
    }

    // 답변글 수정
    @PostMapping("/board/questions/{qid}/answers/{aid}")
    public ResponseEntity<Void> updateAnswer(@PathVariable Long qid, @PathVariable Long aid,
                                             @RequestBody AnswerRequest request) {
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
