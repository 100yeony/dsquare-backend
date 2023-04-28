package com.ktds.dsquare.common.mypage;

import com.ktds.dsquare.board.card.dto.BriefCardResponse;
import com.ktds.dsquare.board.carrot.dto.BriefCarrotResponse;
import com.ktds.dsquare.board.comment.dto.MyCommentInfo;
import com.ktds.dsquare.board.qna.dto.AnswerResponse;
import com.ktds.dsquare.board.qna.dto.BriefQuestionResponse;
import com.ktds.dsquare.board.talk.dto.BriefTalkResponse;
import com.ktds.dsquare.common.annotatin.AuthUser;
import com.ktds.dsquare.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;

    //read - 나의 질문글 전체 조회
    @GetMapping("/mypage/Questions")
    public List<BriefQuestionResponse> getAllMyQuestions(@AuthUser Member user){
        return mypageService.getAllMyQuestions(user);
    }

    //read - 나의 카드주세요 글 전체 조회
    @GetMapping("/mypage/Cards")
    public List<BriefCardResponse> getAllMyCards(@AuthUser Member user){
        return mypageService.getAllMyCards(user);
    }

    //read - 나의 소통해요 글 전체 조회
    @GetMapping("/mypage/Talks")
    public List<BriefTalkResponse> getAllMyTalks(@AuthUser Member user){
        return mypageService.getAllMyTalks(user);
    }

    //read - 나의 당근해요 글 전체 조회
    @GetMapping("/mypage/Carrots")
    public List<BriefCarrotResponse> getAllMyCarrots(@AuthUser Member user){
        return mypageService.getAllMyCarrots(user);
    }

    //read - 나의 답변글 전체 조회
    @GetMapping("/mypage/Answers")
    public List<AnswerResponse> getAllMyAnswers(@AuthUser Member user){
        return mypageService.getAllMyAnswers(user);
    }

    //read - 나의 댓글 전체 조회
    @GetMapping("/mypage/Comments")
    public List<MyCommentInfo> getAllMyComments(@AuthUser Member user){
        return mypageService.getAllMyComments(user);
    }
}
