package com.ktds.dsquare.board.mypage;

import com.ktds.dsquare.board.card.Card;
import com.ktds.dsquare.board.card.CardRepository;
import com.ktds.dsquare.board.card.service.CardService;
import com.ktds.dsquare.board.card.dto.response.BriefCardResponse;
import com.ktds.dsquare.board.carrot.Carrot;
import com.ktds.dsquare.board.carrot.CarrotRepository;
import com.ktds.dsquare.board.carrot.dto.BriefCarrotResponse;
import com.ktds.dsquare.board.comment.Comment;
import com.ktds.dsquare.board.comment.CommentRepository;
import com.ktds.dsquare.board.comment.dto.MyCommentInfo;
import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.board.like.LikeRepository;
import com.ktds.dsquare.board.paging.PagingService;
import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.response.AnswerResponse;
import com.ktds.dsquare.board.qna.dto.response.BriefQuestionResponse;
import com.ktds.dsquare.board.qna.repository.AnswerRepository;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import com.ktds.dsquare.board.qna.service.QuestionService;
import com.ktds.dsquare.board.talk.Talk;
import com.ktds.dsquare.board.talk.TalkRepository;
import com.ktds.dsquare.board.talk.dto.BriefTalkResponse;
import com.ktds.dsquare.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final PagingService pagingService;

    //read - 나의 질문글 전체 조회
    public List<BriefQuestionResponse> getAllMyQuestions(Member user, String order, Pageable pageable){
        Pageable page = pagingService.orderPage(order, pageable);

        Page<Question> questionList = questionRepository.findByDeleteYnAndWriter(false, user, page);
        List<BriefQuestionResponse> searchResults = new ArrayList<>();

        for(Question q: questionList){
            searchResults.add(questionService.makeBriefQuestionRes(q, user));
        }
        return searchResults;
    }

    //read - 나의 카드주세요 글 전체 조회
    public List<BriefCardResponse> getAllMyCards(Member user, String order, Pageable pageable){
        Pageable page = pagingService.orderPage(order, pageable);

        Page<Card> cards = cardRepository.findByDeleteYnAndWriter(false, user, page);
        List<BriefCardResponse> briefCards = new ArrayList<>();

        for(Card C : cards){
            briefCards.add(cardService.makeBriefCardRes(C, user, null));
        }
        return briefCards;
    }

    //read - 나의 소통해요 글 전체 조회
    public List<BriefTalkResponse> getAllMyTalks(Member user, String order, Pageable pageable){
        Pageable page = pagingService.orderPage(order, pageable);

        Page<Talk> talkList = talkRepository.findByDeleteYnAndWriter(false, user, page);
        List<BriefTalkResponse> searchResults = new ArrayList<>();

        for(Talk T: talkList){
            Boolean likeYn = findLikeYn(BoardType.TALK, T.getId(), user);
            Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.TALK, T.getId());
            searchResults.add(BriefTalkResponse.toDto(T, commentCnt, T.getLikeCnt(), likeYn));
        }
        return searchResults;
    }

    //read - 나의 당근해요 글 전체 조회
    public List<BriefCarrotResponse> getAllMyCarrots(Member user, String order, Pageable pageable){
        Pageable page = pagingService.orderPage(order, pageable);

        Page<Carrot> carrotList = carrotRepository.findByDeleteYnAndWriter(false, user, page);
        List<BriefCarrotResponse> searchResults = new ArrayList<>();

        for(Carrot C: carrotList){
            Boolean likeYn = findLikeYn(BoardType.CARROT, C.getId(), user);
            Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.CARROT, C.getId());
            searchResults.add(BriefCarrotResponse.toDto(C, C.getLikeCnt(), likeYn, commentCnt));
        }
        return searchResults;
    }

    //read - 나의 답변글 전체 조회
    public List<AnswerResponse> getAllMyAnswers(Member user, String order, Pageable pageable){
        Pageable page = pagingService.orderPage(order, pageable);

        Page<Answer> answerList = answerRepository.findByDeleteYnAndWriter(false, user, page);
        List<AnswerResponse> searchResults = new ArrayList<>();

        for(Answer A: answerList){
            Boolean likeYn = findLikeYn(BoardType.ANSWER, A.getId(), user);
            Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.ANSWER, A.getId());
            searchResults.add(AnswerResponse.toDto(A, A.getLikeCnt(), likeYn, commentCnt));
        }
        return searchResults;
    }

    //read - 나의 댓글 전체 조회
    public List<MyCommentInfo> getAllMyComments(Member user, Pageable pageable){
        Pageable page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createDate").descending());

        Page<Comment> commentList = commentRepository.findByWriter(user, page);
        List<MyCommentInfo> commentDto = new ArrayList<>();

        for(Comment comment : commentList)
            commentDto.add(MyCommentInfo.toDto(comment));
        return commentDto;
    }

    public Boolean findLikeYn(BoardType boardType, Long postId, Member user){
        return likeRepository.existsByBoardTypeAndPostIdAndMember(boardType, postId, user);
    }

}
