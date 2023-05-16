package com.ktds.dsquare.board.qna.controller;


import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.AnswerRegisterResponse;
import com.ktds.dsquare.board.qna.dto.AnswerRequest;
import com.ktds.dsquare.board.qna.dto.AnswerResponse;
import com.ktds.dsquare.board.qna.service.AnswerService;
import com.ktds.dsquare.common.annotation.AuthUser;
import com.ktds.dsquare.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    //create - 답변글 작성
    @PostMapping("/board/questions/{qid}/answers")
    public ResponseEntity<AnswerRegisterResponse> createAnswer(
            @PathVariable("qid") Long qid,
            @RequestPart(name = "answer") AnswerRequest request,
            @RequestPart(required = false) MultipartFile attachment,
            @AuthUser Member user
    ) {
        return new ResponseEntity<>(answerService.createAnswer(qid, request, user), HttpStatus.CREATED);
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
    public ResponseEntity<Void> updateAnswer(
            @PathVariable Long qid,
            @PathVariable Long aid,
            @RequestPart(name = "answer") AnswerRequest request,
            @RequestPart(name = "attachment", required = false) MultipartFile newAttachment
    ) {
        answerService.updateAnswer(qid, aid, request, newAttachment);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    // 답변글 삭제
    @DeleteMapping("/board/questions/{qid}/answers/{aid}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long qid, @PathVariable Long aid){
        answerService.deleteAnswer(aid);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
