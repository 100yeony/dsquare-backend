package com.ktds.dsquare.board.like;

import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.board.like.dto.LikeRequest;
import com.ktds.dsquare.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    //create - 좋아요 등록(Question, Ansewr, Card) + Talk, Carrot(예정)
    public void like(LikeRequest dto, Member user){
        Like likeCheck = likeRepository.findByBoardTypeAndPostIdAndMember(dto.getBoardType(), dto.getPostId(), user);
        if(likeCheck != null){
            throw new RuntimeException("like already exist");
        }else{
            Like like = Like.toEntity(dto, dto.getBoardType(), user);
            likeRepository.save(like);
        }
    }

    //read - 전체조회(Question 전체조회, Question 상세조회, Answer 조회, Card 전체조회, Card 상세조회)
    //게시글 입장에서 총 좋아요 수
    public Integer findLikeCnt(BoardType boardType, Long postId){
        List<Like> likes = likeRepository.findByBoardTypeAndPostId(boardType, postId);
        return likes.size();
    }

    //로그인한 사용자 입장에서 해당 게시글에 좋아요 여부
    public Boolean findLikeYn(BoardType boardType, Long postId, Member user){
        Like like = likeRepository.findByBoardTypeAndPostIdAndMember(boardType, postId, user);
        if(like != null){
            return true;
        }else{
            return false;
        }
    }


    //delete - 좋아요 취소
    public void dislike(LikeRequest dto, Member user){
        Like like = likeRepository.findByBoardTypeAndPostIdAndMember(dto.getBoardType(), dto.getPostId(), user);
        likeRepository.delete(like);
    }


}
