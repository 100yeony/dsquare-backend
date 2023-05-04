package com.ktds.dsquare.board.like;

import com.ktds.dsquare.board.card.Card;
import com.ktds.dsquare.board.card.CardRepository;
import com.ktds.dsquare.board.card.CardService;
import com.ktds.dsquare.board.carrot.Carrot;
import com.ktds.dsquare.board.carrot.CarrotRepository;
import com.ktds.dsquare.board.carrot.CarrotService;
import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.board.like.dto.LikeRegisterRequest;
import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.repository.AnswerRepository;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import com.ktds.dsquare.board.qna.service.AnswerService;
import com.ktds.dsquare.board.qna.service.QuestionService;
import com.ktds.dsquare.board.talk.Talk;
import com.ktds.dsquare.board.talk.TalkRepository;
import com.ktds.dsquare.board.talk.TalkService;
import com.ktds.dsquare.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final TalkRepository talkRepository;
    private final CardRepository cardRepository;
    private final CarrotRepository carrotRepository;
    private final CardService cardService;
    private final TalkService talkService;
    private final CarrotService carrotService;

    //create - 좋아요 등록
    public void like(LikeRegisterRequest dto, Member user) {
        BoardType boardType = BoardType.findBoardType(dto.getBoardType());
        Boolean likeCheck = likeRepository.existsByBoardTypeAndPostIdAndMember(boardType, dto.getPostId(), user);
        if (likeCheck) {
            throw new RuntimeException("like already exist");
        } else {
            Like like = Like.toEntity(dto, boardType, user);
            likeRepository.save(like);
            switch (dto.getBoardType()) {
                case "question":
                    Question question = questionRepository.findById(dto.getPostId())
                            .orElseThrow(() -> new EntityNotFoundException("question not found"));
                    questionService.like(question);
                    break;
                case "answer":
                    Answer answer = answerRepository.findById(dto.getPostId())
                            .orElseThrow(() -> new EntityNotFoundException("answer not found"));
                    answerService.like(answer);
                case "card":
                    Card card = cardRepository.findById(dto.getPostId())
                            .orElseThrow(() -> new EntityNotFoundException("card not found"));
                    cardService.like(card);
                    break;
                case "talk":
                    Talk talk = talkRepository.findById(dto.getPostId())
                            .orElseThrow(() -> new EntityNotFoundException("talk not found"));
                    talkService.like(talk);
                    break;
                case "carrot":
                    Carrot carrot = carrotRepository.findById(dto.getPostId())
                            .orElseThrow(() -> new EntityNotFoundException("carrot not found"));
                    carrotService.like(carrot);
                    break;
                default:
                    throw new RuntimeException("Invalid board type.");
            }
        }
    }

    //delete - 좋아요 취소
    public void cancelLike(LikeRegisterRequest dto, Member user){
        BoardType boardType = BoardType.findBoardType(dto.getBoardType());
        Like like = likeRepository.findByBoardTypeAndPostIdAndMember(boardType, dto.getPostId(), user);
        likeRepository.delete(like);
        switch (dto.getBoardType()) {
            case "question":
                Question question = questionRepository.findById(dto.getPostId())
                        .orElseThrow(() -> new EntityNotFoundException("question not found"));
                questionService.cancleLike(question);
                break;
            case "answer":
                Answer answer = answerRepository.findById(dto.getPostId())
                        .orElseThrow(() -> new EntityNotFoundException("answer not found"));
                answerService.cancleLike(answer);
            case "card":
                Card card = cardRepository.findById(dto.getPostId())
                        .orElseThrow(() -> new EntityNotFoundException("card not found"));
                cardService.cancleLike(card);
                break;
            case "talk":
                Talk talk = talkRepository.findById(dto.getPostId())
                        .orElseThrow(() -> new EntityNotFoundException("talk not found"));
                talkService.cancleLike(talk);
                break;
            case "carrot":
                Carrot carrot = carrotRepository.findById(dto.getPostId())
                        .orElseThrow(() -> new EntityNotFoundException("carrot not found"));
                carrotService.cancleLike(carrot);
                break;
            default:
                throw new RuntimeException("Invalid board type.");
        }
    }


}
