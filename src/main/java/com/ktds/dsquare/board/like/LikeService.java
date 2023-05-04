package com.ktds.dsquare.board.like;

import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.board.like.dto.LikeRegisterRequest;
import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.repository.AnswerRepository;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import com.ktds.dsquare.board.qna.service.AnswerService;
import com.ktds.dsquare.board.qna.service.QuestionService;
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

    //create - 좋아요 등록(Question, Ansewr, Card) + Talk, Carrot(예정)
    public void like(LikeRegisterRequest dto, Member user){
        BoardType boardType = BoardType.findBoardType(dto.getBoardType());
        Boolean likeCheck = likeRepository.existsByBoardTypeAndPostIdAndMember(boardType, dto.getPostId(), user);
        if(likeCheck){
            throw new RuntimeException("like already exist");
        }else{
            Like like = Like.toEntity(dto, boardType, user);
            likeRepository.save(like);
            if(dto.getBoardType().equals("question")){
                Question question = questionRepository.findById(dto.getPostId())
                                .orElseThrow(()-> new EntityNotFoundException("question not found"));
                questionService.like(question);
            } else if (dto.getBoardType().equals("answer")) {
                Answer answer = answerRepository.findById(dto.getPostId())
                                .orElseThrow(()-> new EntityNotFoundException("answer not found"));
                answerService.like(answer);
            }
        }
    }

    //read - 전체조회(Question 전체조회, Question 상세조회, Answer 조회, Card 전체조회, Card 상세조회)
    //게시글 입장에서 총 좋아요 수
    public Long findLikeCnt(BoardType boardType, Long postId){
        return likeRepository.countByBoardTypeAndPostId(boardType, postId);
    }

    //로그인한 사용자 입장에서 해당 게시글에 좋아요 여부
    public Boolean findLikeYn(BoardType boardType, Long postId, Member user){
        return likeRepository.existsByBoardTypeAndPostIdAndMember(boardType, postId, user);
    }

    //delete - 좋아요 취소
    public void cancelLike(LikeRegisterRequest dto, Member user){
        BoardType boardType = BoardType.findBoardType(dto.getBoardType());
        Like like = likeRepository.findByBoardTypeAndPostIdAndMember(boardType, dto.getPostId(), user);
        likeRepository.delete(like);
        if(dto.getBoardType().equals("question")){
            Question question = questionRepository.findById(dto.getPostId())
                    .orElseThrow(()-> new EntityNotFoundException("question not found"));
            questionService.cancleLike(question);
        } else if (dto.getBoardType().equals("answer")) {
            Answer answer = answerRepository.findById(dto.getPostId())
                    .orElseThrow(()-> new EntityNotFoundException("answer not found"));
            answerService.cancleLike(answer);
        }
    }


}
