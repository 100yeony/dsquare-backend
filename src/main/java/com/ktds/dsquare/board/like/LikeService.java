package com.ktds.dsquare.board.like;

import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.board.like.dto.LikeRegisterRequest;
import com.ktds.dsquare.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    //create - 좋아요 등록(Question, Ansewr, Card) + Talk, Carrot(예정)
    public void like(LikeRegisterRequest dto, Member user){
        BoardType boardType = BoardType.findBoardType(dto.getBoardType());
        Boolean likeCheck = likeRepository.existsByBoardTypeAndPostIdAndMember(boardType, dto.getPostId(), user);
        if(likeCheck){
            throw new RuntimeException("like already exist");
        }else{
            Like like = Like.toEntity(dto, boardType, user);
            likeRepository.save(like);
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
    }


}
