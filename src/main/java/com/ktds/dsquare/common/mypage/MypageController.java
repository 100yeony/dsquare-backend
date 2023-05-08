package com.ktds.dsquare.common.mypage;

import com.ktds.dsquare.board.card.dto.BriefCardResponse;
import com.ktds.dsquare.board.carrot.dto.BriefCarrotResponse;
import com.ktds.dsquare.board.comment.dto.MyCommentInfo;
import com.ktds.dsquare.board.qna.dto.AnswerResponse;
import com.ktds.dsquare.board.qna.dto.BriefQuestionResponse;
import com.ktds.dsquare.board.talk.dto.BriefTalkResponse;
import com.ktds.dsquare.common.annotation.AuthUser;
import com.ktds.dsquare.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;

    //read - 나의 질문글 전체 조회
    @GetMapping("/mypage/questions")
    public List<BriefQuestionResponse> getAllMyQuestions(@AuthUser Member user, @RequestParam(required = false) String order, Pageable pageable){
        return mypageService.getAllMyQuestions(user, order, pageable);
    }

    //read - 나의 카드주세요 글 전체 조회
    @GetMapping("/mypage/cards")
    public List<BriefCardResponse> getAllMyCards(@AuthUser Member user, @RequestParam(required = false) String order, Pageable pageable){
        return mypageService.getAllMyCards(user, order, pageable);
    }

    //read - 나의 소통해요 글 전체 조회
    @GetMapping("/mypage/talks")
    public List<BriefTalkResponse> getAllMyTalks(@AuthUser Member user, @RequestParam(required = false) String order, Pageable pageable){
        return mypageService.getAllMyTalks(user, order, pageable);
    }

    //read - 나의 당근해요 글 전체 조회
    @GetMapping("/mypage/carrots")
    public List<BriefCarrotResponse> getAllMyCarrots(@AuthUser Member user, @RequestParam(required = false) String order, Pageable pageable){
        return mypageService.getAllMyCarrots(user, order, pageable);
    }

    //read - 나의 답변글 전체 조회
    @GetMapping("/mypage/answers")
    public List<AnswerResponse> getAllMyAnswers(@AuthUser Member user, @RequestParam(required = false) String order, Pageable pageable){
        return mypageService.getAllMyAnswers(user, order, pageable);
    }

    //read - 나의 댓글 전체 조회
    @GetMapping("/mypage/comments")
    public List<MyCommentInfo> getAllMyComments(@AuthUser Member user, Pageable pageable){
        return mypageService.getAllMyComments(user, pageable);
    }
}
