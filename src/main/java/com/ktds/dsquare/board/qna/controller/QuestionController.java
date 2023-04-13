package com.ktds.dsquare.board.qna.controller;

import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.QuestionRequest;
import com.ktds.dsquare.board.qna.dto.QuestionResponse;
import com.ktds.dsquare.board.qna.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/board/questions")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/board/questions")
    public ResponseEntity<Void> createQuestion(@RequestBody QuestionRequest request){
        questionService.createQuestion(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //read - 질문글 전체 목록 조회
    @GetMapping("/board/questions")
    public List<Question> getAllQuestions(@RequestParam Boolean workYn){
        return questionService.getAllQuestions(workYn);
    }


    //read - 질문글&답변글 상세 조회
    @GetMapping("/board/questions/{qid}")
    public QuestionResponse getQnADetail(@PathVariable("qid") Long qid) {
        return questionService.getQuestionDetail(qid);
    }

    // 질문글 수정
    @PostMapping("/board/questions/{qid}")
    public ResponseEntity<Void> updateQuestion(@PathVariable("qid") Long qid, @RequestBody QuestionRequest request){
        questionService.updateQuestion(qid, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 질문글 삭제
    @DeleteMapping("/board/questions/{qid}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable("qid") Long qid){
        questionService.deleteQuestion(qid);
        return ResponseEntity.status(HttpStatus.OK).build();
    }



    //search - Q&A 검색(카테고리, 제목+내용, 사용자(이름))
    @GetMapping("/board/questions/search")
    @ResponseBody
    public ResponseEntity<List<Question>> search(@RequestParam(required = false) Integer cid, @RequestParam String key, @RequestParam String value) {
        if (cid != null) {
            List<Question> questions = questionService.searchByCid(cid);
            return ResponseEntity.ok(questions);
        }
        if (key.contentEquals("titleAndContent")) {
            List<Question> questions = questionService.searchByTitleOrContent(value);
            return ResponseEntity.ok(questions);

        } else if (key.contentEquals("member")) {
            //궁금해요 게시판은 사용자 이름으로 검색(member 테이블에서 이름 조회하여 pk 찾기 -> question 테이블에서 writerID로 질문글 찾기)
            //소통해요 게시판은 닉네임으로 검색
            List<Question> questions = questionService.searchByName(value);
            return ResponseEntity.ok(questions);
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

}







