package com.ktds.dsquare.board.qna.controller;

import com.ktds.dsquare.board.qna.dto.request.QuestionRegisterRequest;
import com.ktds.dsquare.board.qna.dto.request.QuestionUpdateRequest;
import com.ktds.dsquare.board.qna.dto.response.QuestionInfo;
import com.ktds.dsquare.board.qna.dto.response.QuestionRegisterResponse;
import com.ktds.dsquare.board.qna.service.QuestionSelectService;
import com.ktds.dsquare.board.qna.service.QuestionService;
import com.ktds.dsquare.common.annotation.AuthUser;
import com.ktds.dsquare.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionSelectService questionSelectService;
    private final QuestionService questionService;

    //create - 질문글 작성
    @PostMapping(value = "/board/questions")//, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<QuestionRegisterResponse> createQuestion(
//            @Parameter @ApiParam(name = "Question 게시글 내용")
            @RequestPart("question") QuestionRegisterRequest request,
//            @ApiParam(name = "Question 게시글 첨부파일")
            @RequestPart(required = false) MultipartFile attachment,
            @ApiIgnore @AuthUser Member writer
    ) throws RuntimeException {
        return new ResponseEntity<>(questionService.createQuestion(request, attachment, writer), HttpStatus.CREATED);
    }

    //read - 질문글 전체 목록 조회 & 검색
    @GetMapping("/board/questions")
//    public List<BriefQuestionResponse> getQuestions(@RequestParam Boolean workYn, @ApiIgnore @AuthUser Member user, @RequestParam(required = false) Integer cid,
//                                                    @RequestParam(required = false) String key, @RequestParam(required = false) String value,
//                                                    @RequestParam(required = false) String order, Pageable pageable) {
//        return questionService.getQuestions(workYn, user, cid, key, value, order, pageable);
//    }
    public ResponseEntity<?> getQuestions(
            @RequestParam Map<String, String> params,
            Pageable pageable,
            @AuthUser Member user
    ) {
        return new ResponseEntity<>(questionSelectService.getQuestions(params, pageable, user), HttpStatus.OK);
    }

    //read - 질문글 상세 조회
//    @GetMapping("/board/questions/{qid}")
//    public ResponseEntity<QuestionResponse> getQnADetail(@AuthUser Member user, @PathVariable("qid") Long qid) {
//        return ResponseEntity.ok(questionService.getQuestionDetail(user, qid));
//    }
    @GetMapping("/board/questions/{id}")
    public ResponseEntity<QuestionInfo> getQuestion(@PathVariable long id, @AuthUser Member user) {
        return new ResponseEntity<>(questionSelectService.getQuestion(id, user), HttpStatus.OK);
    }


    // 질문글 수정
//    @PatchMapping("/board/questions/{qid}")
//    public ResponseEntity<Void> updateQuestion(
//            @PathVariable("qid") Long qid,
//            @RequestPart("question") QuestionRequest request,
//            @RequestPart(name = "attachment", required = false) MultipartFile newAttachment
//    ) {
//        questionService.updateQuestion(qid, request, newAttachment);
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }
    @PatchMapping("/board/questions/{id}")
    public ResponseEntity<QuestionRegisterResponse> updateQuestion(
            @PathVariable long id,
            @RequestPart("question") QuestionUpdateRequest request,
            @RequestPart(name = "attachment", required = false) MultipartFile newAttachment
    ) {
        return new ResponseEntity<>(questionService.updateQuestion(id, request, newAttachment), HttpStatus.OK);
    }

    // 질문글 삭제
    @DeleteMapping("/board/questions/{qid}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable("qid") Long qid){
        questionService.deleteQuestion(qid);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
