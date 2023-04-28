package com.ktds.dsquare.common.mypage;

import com.ktds.dsquare.board.card.Card;
import com.ktds.dsquare.board.card.CardRepository;
import com.ktds.dsquare.board.card.dto.BriefCardResponse;
import com.ktds.dsquare.board.card.dto.CardSelectionInfo;
import com.ktds.dsquare.board.carrot.Carrot;
import com.ktds.dsquare.board.carrot.CarrotRepository;
import com.ktds.dsquare.board.carrot.dto.BriefCarrotResponse;
import com.ktds.dsquare.board.comment.Comment;
import com.ktds.dsquare.board.comment.CommentRepository;
import com.ktds.dsquare.board.comment.dto.MyCommentInfo;
import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.board.like.LikeService;
import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.AnswerResponse;
import com.ktds.dsquare.board.qna.dto.BriefQuestionResponse;
import com.ktds.dsquare.board.qna.dto.CategoryResponse;
import com.ktds.dsquare.board.qna.repository.AnswerRepository;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import com.ktds.dsquare.board.talk.Talk;
import com.ktds.dsquare.board.talk.TalkRepository;
import com.ktds.dsquare.board.talk.dto.BriefTalkResponse;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.dto.response.MemberInfo;
import com.ktds.dsquare.member.dto.response.TeamInfo;
import com.ktds.dsquare.member.team.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final LikeService likeService;
    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;
    private final TalkRepository talkRepository;
    private final CarrotRepository carrotRepository;

    //read - 나의 질문글 전체 조회
    public List<BriefQuestionResponse> getAllMyQuestions(Member user){
        List<Question> questionList = questionRepository.findByDeleteYnAndWriter(false, user);
        List<BriefQuestionResponse> searchResults = new ArrayList<>();

        for(Question q: questionList){
            CategoryResponse categoryRes = CategoryResponse.toDto(q.getCategory());
            List<Answer> answers = answerRepository.findByQuestionAndDeleteYn(q, false);
            boolean managerAnswerYn = false;
            for (Answer A : answers) {
                if (q.getCategory().getManagerId()==A.getWriter().getId()) {
                    managerAnswerYn = true;
                    break;
                }
            }
            Long likeCnt = likeService.findLikeCnt(BoardType.QUESTION, q.getQid());
            Boolean likeYn = likeService.findLikeYn(BoardType.QUESTION, q.getQid(), user);
            Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.QUESTION, q.getQid());
            searchResults.add(BriefQuestionResponse.toDto(q, MemberInfo.toDto(q.getWriter()),categoryRes ,(long)answers.size(), managerAnswerYn, likeCnt, likeYn, commentCnt));
        }
        return searchResults;
    }

    //read - 나의 카드주세요 글 전체 조회
    public List<BriefCardResponse> getAllMyCards(Member user){
        List<Card> cards = cardRepository.findByDeleteYnAndWriter(false, user);
        List<BriefCardResponse> briefCards = new ArrayList<>();

        for(Card C : cards){
            Member member = C.getWriter();
            Member owner = C.getCardOwner();
            CardSelectionInfo selectionInfo;
            Team team = C.getProjTeam();
            if(owner != null){
                MemberInfo cardOwner = MemberInfo.toDto(owner);
                selectionInfo = CardSelectionInfo.toDto(C, cardOwner);
            }else{
                selectionInfo = null;
            }

            Long likeCnt = likeService.findLikeCnt(BoardType.CARD, C.getId());
            Boolean likeYn = likeService.findLikeYn(BoardType.CARD, C.getId(), user);
            Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.CARD, C.getId());
            briefCards.add(BriefCardResponse.toDto(C, MemberInfo.toDto(member), TeamInfo.toDto(team), selectionInfo, likeCnt, likeYn, commentCnt));
        }

        return briefCards;
    }

    //read - 나의 소통해요 글 전체 조회
    public List<BriefTalkResponse> getAllMyTalks(Member user){
        List<Talk> talkList = talkRepository.findByDeleteYnAndWriter(false, user);
        List<BriefTalkResponse> searchResults = new ArrayList<>();

        for(Talk T: talkList){
            Long likeCnt = likeService.findLikeCnt(BoardType.TALK, T.getId());
            Boolean likeYn = likeService.findLikeYn(BoardType.TALK, T.getId(), user);
            Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.TALK, T.getId());
            searchResults.add(BriefTalkResponse.toDto(T, commentCnt, likeCnt, likeYn));
        }
        return searchResults;
    }

    //read - 나의 당근해요 글 전체 조회
    public List<BriefCarrotResponse> getAllMyCarrots(Member user){
        List<Carrot> carrotList = carrotRepository.findByDeleteYnAndWriter(false, user);
        List<BriefCarrotResponse> searchResults = new ArrayList<>();

        for(Carrot C: carrotList){
            Member member = C.getWriter();
            Long likeCnt = likeService.findLikeCnt(BoardType.CARROT, C.getId());
            Boolean likeYn = likeService.findLikeYn(BoardType.CARROT, C.getId(), user);
            Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.CARROT, C.getId());
            searchResults.add(BriefCarrotResponse.toDto(C, MemberInfo.toDto(member), likeCnt, likeYn, commentCnt));
        }
        return searchResults;
    }

    //read - 나의 답변글 전체 조회
    public List<AnswerResponse> getAllMyAnswers(Member user){
        List<Answer> answerList = answerRepository.findByDeleteYnAndWriter(false, user);
        List<AnswerResponse> searchResults = new ArrayList<>();

        for(Answer A: answerList){
            Member member = A.getWriter();
            Long likeCnt = likeService.findLikeCnt(BoardType.ANSWER, A.getId());
            Boolean likeYn = likeService.findLikeYn(BoardType.ANSWER, A.getId(), user);
            Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.ANSWER, A.getId());
            searchResults.add(AnswerResponse.toDto(A, MemberInfo.toDto(member), likeCnt, likeYn, commentCnt));
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

}
