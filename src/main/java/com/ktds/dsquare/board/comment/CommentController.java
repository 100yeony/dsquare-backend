package com.ktds.dsquare.board.comment;

import com.ktds.dsquare.board.comment.dto.CommentRequest;
import com.ktds.dsquare.board.comment.dto.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/board/{boardTypeId}/{postId}/comments")
    public ResponseEntity<Void> createComment(@PathVariable Long boardTypeId, @PathVariable Long postId, @RequestBody CommentRequest request){
        commentService.createComment(boardTypeId, postId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 댓글 조회(글에 달린 댓글 전체 조회)
    @GetMapping("/board/{boardTypeId}/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getAllComments(@PathVariable Long boardTypeId, @PathVariable Long postId){
        return new ResponseEntity<>(commentService.getAllComments(boardTypeId, postId), HttpStatus.OK);
    }

    // 댓글 삭제
    @DeleteMapping("/board/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
