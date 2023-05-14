package com.ktds.dsquare.board.comment;

import com.ktds.dsquare.board.card.CardRepository;
import com.ktds.dsquare.board.carrot.CarrotRepository;
import com.ktds.dsquare.board.comment.dto.*;
import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.board.qna.repository.AnswerRepository;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import com.ktds.dsquare.board.talk.TalkRepository;
import com.ktds.dsquare.common.exception.BoardTypeException;
import com.ktds.dsquare.common.exception.LackOfDataException;
import com.ktds.dsquare.common.exception.PostNotFoundException;
import com.ktds.dsquare.common.exception.UserNotFoundException;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
    private final CardRepository cardRepository;
    private final TalkRepository talkRepository;
    private final CarrotRepository carrotRepository;

    // 댓글 작성
    @Transactional
    public CommentRegisterResponse createComment(String boardTypeName, Long postId, CommentRegisterDto request, Member user) {
        if(request.getContent().equals(""))
            throw new LackOfDataException("There is no content.");
        if(!checkAvailability(boardTypeName, postId))
            throw new PostNotFoundException("Post Not Found. Board Type: "+boardTypeName+", Post ID: "+postId);
        BoardType boardType = BoardType.findBoardType(boardTypeName);
        Comment comment = Comment.toEntity(request, user, boardType, postId);
        return CommentRegisterResponse.toDto(commentRepository.save(comment));
    }

    // 대댓글 작성
    @Transactional
    public void createNestedComment(String boardTypeName, Long postId, NestedCommentRegisterDto request, Member user) {
        if(request.getContent().equals(""))
            throw new LackOfDataException("There is no content.");
        if(!checkAvailability(boardTypeName, postId))
            throw new PostNotFoundException("Post Not Found. Board Type: "+boardTypeName+", Post ID: "+postId);
        Member originWriter = memberRepository.findById(request.getOriginWriterId()).orElseThrow(() -> new UserNotFoundException("Origin Writer Not Found"));
        BoardType boardType = BoardType.findBoardType(boardTypeName);
        Comment comment = Comment.toNestedEntity(request, user, boardType, postId, originWriter);
        commentRepository.save(comment);
    }

    // 댓글 조회(글에 달린 댓글 전체 조회)
    public List<Object> getAllComments(String boardTypeName, Long postId, Pageable pageable) {
        Pageable page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createDate"));

        if(!checkAvailability(boardTypeName, postId))
            throw new PostNotFoundException("Post Not Found. Board Type: "+boardTypeName+", Post ID: "+postId);
        BoardType boardType = BoardType.findBoardType(boardTypeName);
        Page<Comment> comments = commentRepository.findByBoardTypeAndPostId(boardType, postId, page);
        List<Object> commentDto = new ArrayList<>();
        for(Comment comment : comments)
            if(ObjectUtils.isEmpty(comment.getOriginWriter()))
                commentDto.add(CommentInfo.toDto(comment));
            else if(boardType.equals(BoardType.TALK))
                commentDto.add(NestedTalkCommentInfo.toDto(comment));
            else
                commentDto.add(NestedCommentInfo.toDto(comment));
        return commentDto;
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new PostNotFoundException("Comment Not Found. Comment ID: "+commentId));
        commentRepository.delete(comment);
    }

    //원글이 삭제되면 댓글도 함께 삭제
    public void deleteCommentCascade(BoardType boardType, Long postId){
        List<Comment> comments = commentRepository.findByBoardTypeAndPostId(boardType, postId);
        commentRepository.deleteAllInBatch(comments);
    }

    // boardTypeId, postId로 해당 글이 존재하는지 확인(true: 글 있음, false: 없음)
    public boolean checkAvailability(String boardTypeName, Long postId) {
        switch(boardTypeName) {
            case "question":
                return questionRepository.findByDeleteYnAndQid(false, postId) != null; // 찾는 post 있으면 true
            case "answer":
                return answerRepository.findByDeleteYnAndId(false, postId) != null;
            case "card":
                return cardRepository.findByDeleteYnAndId(false, postId) != null;
            case "talk":
                return talkRepository.findByDeleteYnAndId(false, postId) != null;
            case "carrot":
                return carrotRepository.findByDeleteYnAndId(false, postId) != null;
            default:
                throw new BoardTypeException("BoardTypeName Not Found");
        }
    }
}
