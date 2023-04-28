package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.comment.CommentRepository;
import com.ktds.dsquare.board.comment.CommentService;
import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.board.like.LikeService;
import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.AnswerRequest;
import com.ktds.dsquare.board.qna.dto.AnswerResponse;
import com.ktds.dsquare.board.qna.repository.AnswerRepository;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.dto.response.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final LikeService likeService;
    private final CommentRepository commentRepository;
    private final CommentService commentService;

    // 답변글 작성
    @Transactional
    public void createAnswer(Long qid, AnswerRequest dto, Member user) {
        Question question = questionRepository.findById(qid).orElseThrow(() -> new EntityNotFoundException("Question not found"));
        Answer answer = Answer.toEntity(dto, user, question);
        answerRepository.save(answer);
    }

    // 답변글 조회
    public List<AnswerResponse> getAnswersByQuestion(Question qid, Member user) {
        List<AnswerResponse> answerResponses = new ArrayList<>();
        List<Answer> answers = answerRepository.findByQuestionAndDeleteYnOrderByCreateDateAsc(qid, false);
        for(Answer answer:answers){
            Long likeCnt = likeService.findLikeCnt(BoardType.ANSWER, answer.getId());
            Boolean likeYn = likeService.findLikeYn(BoardType.ANSWER, answer.getId(), user);
            Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.ANSWER, qid.getQid());
            answerResponses.add(AnswerResponse.toDto(answer, MemberInfo.toDto(answer.getWriter()), likeCnt, likeYn, commentCnt));
        }
        return answerResponses;
    }

    // 답변글 수정
    @Transactional
    public void updateAnswer(Long aid, AnswerRequest request) {
        Answer answer = answerRepository.findByDeleteYnAndId(false, aid);
        if(answer==null){
            throw new EntityNotFoundException("answer not found. aid is " + aid);
        }
        answer.updateAnswer(request.getContent(), request.getAtcId());
    }

    // 답변글 삭제
    @Transactional
    public void deleteAnswer(Long aid) {
        Answer answer = answerRepository.findById(aid).orElseThrow(() -> new EntityNotFoundException("Answer does not exist"));
        answer.deleteAnswer();
        commentService.deleteCommentCascade(BoardType.ANSWER, aid);
    }

}
