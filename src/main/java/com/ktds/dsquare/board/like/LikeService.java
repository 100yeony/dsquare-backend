package com.ktds.dsquare.board.like;

import com.ktds.dsquare.board.card.CardService;
import com.ktds.dsquare.board.carrot.CarrotService;
import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.board.like.dto.LikeRegisterRequest;
import com.ktds.dsquare.board.qna.service.AnswerService;
import com.ktds.dsquare.board.qna.service.QuestionService;
import com.ktds.dsquare.board.talk.TalkService;
import com.ktds.dsquare.common.exception.BoardTypeException;
import com.ktds.dsquare.common.exception.DislikeException;
import com.ktds.dsquare.common.exception.LikeException;
import com.ktds.dsquare.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final CardService cardService;
    private final TalkService talkService;
    private final CarrotService carrotService;

    //create - 좋아요 등록
    @Transactional
    public void like(LikeRegisterRequest dto, Member user) {
        BoardType boardType = BoardType.findBoardType(dto.getBoardType());
        Boolean likeCheck = likeRepository.existsByBoardTypeAndPostIdAndMember(boardType, dto.getPostId(), user);
        if (likeCheck) {
            throw new LikeException("Like already exist");
        } else {
            Like like = Like.toEntity(dto, boardType, user);
            likeRepository.save(like);
            switch (boardType) {
                case QUESTION:
                    questionService.like(dto.getPostId());
                    break;
                case ANSWER:
                    answerService.like(dto.getPostId());
                case CARD:
                    cardService.like(dto.getPostId());
                    break;
                case TALK:
                    talkService.like(dto.getPostId());
                    break;
                case CARROT:
                    carrotService.like(dto.getPostId());
                    break;
                default:
                    throw new BoardTypeException("Invalid board type.");
            }
        }
    }

    //delete - 좋아요 취소
    @Transactional
    public void cancelLike(LikeRegisterRequest dto, Member user){
        BoardType boardType = BoardType.findBoardType(dto.getBoardType());
        Like like = likeRepository.findByBoardTypeAndPostIdAndMember(boardType, dto.getPostId(), user);
        if(like == null)
            throw new DislikeException("There is no Like to delete.");
        likeRepository.delete(like);
        switch (boardType) {
            case QUESTION:
                questionService.cancleLike(dto.getPostId());
                break;
            case ANSWER:
                answerService.cancleLike(dto.getPostId());
            case CARD:
                cardService.cancleLike(dto.getPostId());
                break;
            case TALK:
                talkService.cancleLike(dto.getPostId());
                break;
            case CARROT:
                carrotService.cancleLike(dto.getPostId());
                break;
            default:
                throw new BoardTypeException("Invalid board type.");
        }
    }


}
