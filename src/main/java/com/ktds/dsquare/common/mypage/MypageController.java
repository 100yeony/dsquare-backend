package com.ktds.dsquare.common.mypage;

import com.ktds.dsquare.board.card.dto.BriefCardResponse;
import com.ktds.dsquare.board.carrot.dto.BriefCarrotResponse;
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
    @GetMapping("/mypage/myQuestions")
    public List<BriefQuestionResponse> getAllMyQuestions(@AuthUser Member user){
        return mypageService.getAllMyQuestions(user);
    }

    //read - 나의 카드주세요 글 전체 조회
    @GetMapping("/mypage/myCards")
    public List<BriefCardResponse> getAllMyCards(@AuthUser Member user){
        return mypageService.getAllMyCards(user);
    }

    //read - 나의 소통해요 글 전체 조회
    @GetMapping("/mypage/myTalks")
    public List<BriefTalkResponse> getAllMyTalks(@AuthUser Member user){
        return mypageService.getAllMyTalks(user);
    }

    //read - 나의 당근해요 글 전체 조회
    @GetMapping("/mypage/myCarrots")
    public List<BriefCarrotResponse> getAllMyCarrots(@AuthUser Member user){
        return mypageService.getAllMyCarrots(user);
    }
}
