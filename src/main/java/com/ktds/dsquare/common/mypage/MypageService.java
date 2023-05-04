package com.ktds.dsquare.common.mypage;

import com.ktds.dsquare.board.card.Card;
import com.ktds.dsquare.board.card.CardRepository;
import com.ktds.dsquare.board.card.CardService;
import com.ktds.dsquare.board.card.dto.BriefCardResponse;
import com.ktds.dsquare.board.carrot.Carrot;
import com.ktds.dsquare.board.carrot.CarrotRepository;
import com.ktds.dsquare.board.carrot.dto.BriefCarrotResponse;
import com.ktds.dsquare.board.comment.Comment;
import com.ktds.dsquare.board.comment.CommentRepository;
import com.ktds.dsquare.board.comment.dto.MyCommentInfo;
import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.board.like.LikeRepository;
import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.AnswerResponse;
import com.ktds.dsquare.board.qna.dto.BriefQuestionResponse;
import com.ktds.dsquare.board.qna.repository.AnswerRepository;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import com.ktds.dsquare.board.qna.service.QuestionService;
import com.ktds.dsquare.board.talk.Talk;
import com.ktds.dsquare.board.talk.TalkRepository;
import com.ktds.dsquare.board.talk.dto.BriefTalkResponse;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.dto.response.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final QuestionRepository questionRepository;
    private final QuestionService questionService;
    private final AnswerRepository answerRepository;
    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;
    private final TalkRepository talkRepository;
    private final CarrotRepository carrotRepository;
    private final CardService cardService;
    private final LikeRepository likeRepository;

    //read - 나의 질문글 전체 조회
    public List<BriefQuestionResponse> getAllMyQuestions(Member user){
        List<Question> questionList = questionRepository.findByDeleteYnAndWriter(false, user);
        List<BriefQuestionResponse> searchResults = new ArrayList<>();

        for(Question q: questionList){
            searchResults.add(questionService.makeBriefQuestionRes(q, user));
        }
        return searchResults;
    }

    //read - 나의 카드주세요 글 전체 조회
    public List<BriefCardResponse> getAllMyCards(Member user){
        List<Card> cards = cardRepository.findByDeleteYnAndWriter(false, user);
        List<BriefCardResponse> briefCards = new ArrayList<>();

        for(Card C : cards){
            briefCards.add(cardService.makeBriefCardRes(C, user, null));
        }
        return briefCards;
    }

    //read - 나의 소통해요 글 전체 조회
    public List<BriefTalkResponse> getAllMyTalks(Member user){
        List<Talk> talkList = talkRepository.findByDeleteYnAndWriter(false, user);
        List<BriefTalkResponse> searchResults = new ArrayList<>();

        for(Talk T: talkList){
            Boolean likeYn = findLikeYn(BoardType.TALK, T.getId(), user);
            Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.TALK, T.getId());
            searchResults.add(BriefTalkResponse.toDto(T, commentCnt, T.getLikeCnt(), likeYn));
        }
        return searchResults;
    }

    //read - 나의 당근해요 글 전체 조회
    public List<BriefCarrotResponse> getAllMyCarrots(Member user){
        List<Carrot> carrotList = carrotRepository.findByDeleteYnAndWriter(false, user);
        List<BriefCarrotResponse> searchResults = new ArrayList<>();

        for(Carrot C: carrotList){
            Member member = C.getWriter();
            Boolean likeYn = findLikeYn(BoardType.CARROT, C.getId(), user);
            Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.CARROT, C.getId());
            searchResults.add(BriefCarrotResponse.toDto(C, MemberInfo.toDto(member), C.getLikeCnt(), likeYn, commentCnt));
        }
        return searchResults;
    }

    //read - 나의 답변글 전체 조회
    public List<AnswerResponse> getAllMyAnswers(Member user){
        List<Answer> answerList = answerRepository.findByDeleteYnAndWriter(false, user);
        List<AnswerResponse> searchResults = new ArrayList<>();

        for(Answer A: answerList){
            Member member = A.getWriter();
            Boolean likeYn = findLikeYn(BoardType.ANSWER, A.getId(), user);
            Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.ANSWER, A.getId());
            searchResults.add(AnswerResponse.toDto(A, MemberInfo.toDto(member), A.getLikeCnt(), likeYn, commentCnt));
        }
        return searchResults;
    }

    //read - 나의 댓글 전체 조회
    public List<MyCommentInfo> getAllMyComments(Member user){
        List<Comment> commentList = commentRepository.findByWriter(user);
        List<MyCommentInfo> commentDto = new ArrayList<>();

        for(Comment comment : commentList)
            commentDto.add(MyCommentInfo.toDto(comment));
        return commentDto;
    }

    public Boolean findLikeYn(BoardType boardType, Long postId, Member user){
        return likeRepository.existsByBoardTypeAndPostIdAndMember(boardType, postId, user);
    }

}
