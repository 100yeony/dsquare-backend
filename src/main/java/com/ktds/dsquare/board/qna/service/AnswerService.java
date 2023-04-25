package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.comment.CommentService;
import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.AnswerRequest;
import com.ktds.dsquare.board.qna.dto.AnswerResponse;
import com.ktds.dsquare.board.qna.repository.AnswerRepository;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.MemberRepository;
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
    private final MemberRepository memberRepository;
    private final CommentService commentService;

    // 답변글 작성
    @Transactional
    public void createAnswer(Long qid, AnswerRequest dto) {
        Question question = questionRepository.findById(qid).orElseThrow(() -> new EntityNotFoundException("Question not found"));
        Member writer = memberRepository.findById(dto.getWriterId()).orElseThrow(() -> new EntityNotFoundException("Question not found"));
        Answer answer = Answer.toEntity(dto, writer, question);
        answerRepository.save(answer);
    }

    // 답변글 조회
    public List<AnswerResponse> getAnswersByQuestion(Question qid) {
        List<AnswerResponse> answerResponses = new ArrayList<>();
        List<Answer> answers = answerRepository.findByQuestionAndDeleteYnOrderByCreateDateAsc(qid, false);
        for(Answer answer:answers){
            Long commentCnt = (long) commentService.getAllComments("answer", qid.getQid()).size();
            answerResponses.add(AnswerResponse.toDto(answer, MemberInfo.toDto(answer.getWriter()), commentCnt));
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
    }

}
