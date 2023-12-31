package com.ktds.dsquare.board.comment;

import com.ktds.dsquare.board.comment.dto.CommentRegisterDto;
import com.ktds.dsquare.board.comment.dto.CommentRegisterResponse;
import com.ktds.dsquare.board.comment.dto.NestedCommentRegisterDto;
import com.ktds.dsquare.common.annotation.AuthUser;
import com.ktds.dsquare.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/board/{boardTypeName}/{postId}/comments")
    public ResponseEntity<CommentRegisterResponse> createComment(@PathVariable String boardTypeName, @PathVariable Long postId,
                                                                 @RequestBody CommentRegisterDto request, @AuthUser Member user){
        return new ResponseEntity<>(commentService.createComment(boardTypeName, postId, request, user), HttpStatus.CREATED);
    }

    // 대댓글 작성
    @PostMapping("/board/{boardTypeName}/{postId}/comments/{nestedCommentId}")
    public ResponseEntity<Void> createNestedComment(@PathVariable String boardTypeName, @PathVariable Long postId,
                                                    @RequestBody NestedCommentRegisterDto request, @AuthUser Member user){
        commentService.createNestedComment(boardTypeName, postId, request, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 댓글 조회(글에 달린 댓글 전체 조회)
    @GetMapping("/board/{boardTypeName}/{postId}/comments")
    public ResponseEntity<List<Object>> getAllComments(@PathVariable String boardTypeName, @PathVariable Long postId, Pageable pageable){
        return new ResponseEntity<>(commentService.getAllComments(boardTypeName, postId, pageable), HttpStatus.OK);
    }

    // 댓글 삭제
    @DeleteMapping("/board/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
