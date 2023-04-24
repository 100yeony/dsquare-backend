package com.ktds.dsquare.board.comment;

import com.ktds.dsquare.board.comment.dto.CommentRequest;
import com.ktds.dsquare.board.comment.dto.CommentResponse;
import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.board.qna.repository.AnswerRepository;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    // 댓글 작성
    @Transactional
    public void createComment(Long boardTypeId, Long postId, CommentRequest request) {
        if(!checkAvailability(boardTypeId, postId))
            throw new RuntimeException("Post Not Found");
        Member writer = memberRepository.findById(request.getWriterId()).orElseThrow(() -> new RuntimeException("Writer Not Found"));
        Member originWriter = null;
        if (request.getOriginWriterId() != null)
            originWriter = memberRepository.findById(request.getOriginWriterId()).orElseThrow(() -> new RuntimeException("Origin Writer Not Found"));
        BoardType boardType = BoardType.findBoardType(boardTypeId);
        Comment comment = Comment.toEntity(request, writer, boardType, postId, originWriter);
        commentRepository.save(comment);
    }

    // 댓글 조회(글에 달린 댓글 전체 조회)
    public List<CommentResponse> getAllComments(Long boardTypeId, Long postId) {
        if(!checkAvailability(boardTypeId, postId))
            throw new RuntimeException("Post Not Found");
        BoardType boardType = BoardType.findBoardType(boardTypeId);
        List<Comment> comments = commentRepository.findByBoardTypeAndPostId(boardType, postId);
        List<CommentResponse> commentDto = new ArrayList<>();
        for(Comment comment : comments)
            commentDto.add(CommentResponse.toDto(comment));
        return commentDto;
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment Not Found"));
        commentRepository.delete(comment);
    }

    // boardTypeId, postId로 해당 글이 존재하는지 확인(true: 글 있음, false: 없음)
    public boolean checkAvailability(Long boardTypeId, Long postId) {
        switch(boardTypeId.intValue()) {
            case 0:
                return questionRepository.findByDeleteYnAndQid(false, postId) != null; // 찾는 post 있으면 true
            case 1:
                return answerRepository.findByDeleteYnAndId(false, postId) != null;
//            case 2:
//                return cardRepository.findByDeleteYnAndId(false, postId) != null;
//            case 3:
//                return talkRepository.findByDeleteYnAndId(false, postId) != null;
//            case 4:
//                return carrotRepository.findByDeleteYnAndId(false, postId) != null;
            default:
                throw new RuntimeException("BoardTypeId Not Found");
        }
    }
}
