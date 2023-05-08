package com.ktds.dsquare.board.qna.controller;


import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.AnswerRequest;
import com.ktds.dsquare.board.qna.dto.AnswerResponse;
import com.ktds.dsquare.board.qna.service.AnswerService;
import com.ktds.dsquare.common.annotation.AuthUser;
import com.ktds.dsquare.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    //create - 답변글 작성
    @PostMapping("/board/questions/{qid}/answers")
    public ResponseEntity<Void> createAnswer(@PathVariable("qid") Long qid, @RequestBody AnswerRequest request, @AuthUser Member user){
        answerService.createAnswer(qid, request, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //qid와 연결된 답변 모두 조회
    @GetMapping("/board/questions/{qid}/answers")
    public List<AnswerResponse> getAnswersByQid(@PathVariable Question qid, @AuthUser Member user) {
        return answerService.getAnswersByQuestion(qid, user);
    }

    //답변 상세 조회
    @GetMapping("/board/answers/{aid}")
    public AnswerResponse getAnswerDetail(@PathVariable Long aid, @AuthUser Member user){
        return answerService.getAnswerDetail(aid, user);
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
