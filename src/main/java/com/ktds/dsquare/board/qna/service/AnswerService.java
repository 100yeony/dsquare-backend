package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.comment.CommentRepository;
import com.ktds.dsquare.board.comment.CommentService;
import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.board.like.LikeRepository;
import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.AnswerRequest;
import com.ktds.dsquare.board.qna.dto.AnswerResponse;
import com.ktds.dsquare.board.qna.repository.AnswerRepository;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import com.ktds.dsquare.common.file.Attachment;
import com.ktds.dsquare.common.file.AttachmentService;
import com.ktds.dsquare.common.file.dto.AttachmentDto;
import com.ktds.dsquare.common.exception.PostNotFoundException;
import com.ktds.dsquare.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {

    /*** Service ***/
    private final CommentService commentService;
    private final AttachmentService attachmentService;

    /*** Repository ***/
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    // 답변글 작성
    @Transactional
    public void createAnswer(Long qid, AnswerRequest dto, MultipartFile attachment, Member user) {
        Question question = questionRepository.findByDeleteYnAndQid(false, qid)
                .orElseThrow(() -> new PostNotFoundException("Question not found. Question ID: " + qid));
        Answer answer = Answer.toEntity(dto, user, question);
        saveAttachment(attachment, answer);
        answerRepository.save(answer);
    }
    @Transactional // TODO code duplication
    public void saveAttachment(MultipartFile attachment, Answer answer) {
        Attachment savedAttachment = attachmentService.saveAttachment(answer.getWriter(), attachment, answer);
        if (savedAttachment != null)
            savedAttachment.linkPost(answer);
    }

    // 답변글 전체 조회(질문 번호로 조회)
    public List<AnswerResponse> getAnswersByQuestion(Question qid, Member user) {
        List<AnswerResponse> answerResponses = new ArrayList<>();
        List<Answer> answers = answerRepository.findByQuestionAndDeleteYnOrderByCreateDateAsc(qid, false);
        for(Answer answer:answers){
            Boolean likeYn = findLikeYn(BoardType.ANSWER, answer.getId(), user);
            Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.ANSWER, answer.getId());
            answerResponses.add(AnswerResponse.toDto(answer, answer.getLikeCnt(), likeYn, commentCnt));
        }
        return answerResponses;
    }

    //답변글 상세 조회
    public AnswerResponse getAnswerDetail(Long aid, Member user){
        Answer answer = answerRepository.findByDeleteYnAndId(false, aid)
                .orElseThrow(() -> new PostNotFoundException("Answer Not Found. Answer ID: "+aid));
        Boolean likeYn = findLikeYn(BoardType.ANSWER, answer.getId(), user);
        Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.ANSWER, answer.getId());
        return AnswerResponse.toDto(answer, answer.getLikeCnt(), likeYn, commentCnt);
    }


    // 답변글 수정
    @Transactional
    public void updateAnswer(Long qid, Long aid, AnswerRequest request, MultipartFile newAttachment) {
        // TODO validate qid <> aid.qid
        Answer answer = answerRepository.findByDeleteYnAndId(false, aid)
                .orElseThrow(() -> new PostNotFoundException("Answer Not Found. Answer ID: "+aid));
        if(answer==null){
            throw new EntityNotFoundException("answer not found. aid is " + aid);
        }
        answer.updateAnswer(request.getContent());
        updateAttachment(request.getAttachment(), newAttachment, answer);
    }
    @Transactional
    public void updateAttachment(AttachmentDto attachment, MultipartFile newAttachment, Answer answer) {
        attachmentService.updateAttachment(attachment, answer.getAttachment());
        saveAttachment(newAttachment, answer);
    }

    // 답변글 삭제
    @Transactional
    public void deleteAnswer(Long aid) {
        Answer answer = answerRepository.findByDeleteYnAndId(false, aid)
                .orElseThrow(() -> new PostNotFoundException("Answer Not Found. Answer ID: "+aid));
        answer.deleteAnswer();
        commentService.deleteCommentCascade(BoardType.ANSWER, aid);
    }

    public void like(Long id) {
        Answer answer = answerRepository.findByDeleteYnAndId(false, id)
                .orElseThrow(() -> new PostNotFoundException("Answer Not Found. Answer ID: "+id));
        answer.like();
    }


    public void cancleLike(Long id){
        Answer answer = answerRepository.findByDeleteYnAndId(false, id)
                .orElseThrow(() -> new PostNotFoundException("Answer Not Found. Answer ID: "+id));
        answer.cancleLike();
    }

    public Boolean findLikeYn(BoardType boardType, Long postId, Member user){
        return likeRepository.existsByBoardTypeAndPostIdAndMember(boardType, postId, user);
    }

}
