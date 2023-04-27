package com.ktds.dsquare.board.like;

import com.ktds.dsquare.board.like.dto.LikeRegisterRequest;
import com.ktds.dsquare.common.annotatin.AuthUser;
import com.ktds.dsquare.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    //좋아요 등록
    @PostMapping("/board/like")
    public ResponseEntity<Void> like(@RequestBody LikeRegisterRequest request, @AuthUser Member user){
        likeService.like(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //좋아요 취소
    @DeleteMapping("/board/like")
    public ResponseEntity<Void> cancelLike(@RequestBody LikeRegisterRequest request, @AuthUser Member user){
        likeService.cancelLike(request, user);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
