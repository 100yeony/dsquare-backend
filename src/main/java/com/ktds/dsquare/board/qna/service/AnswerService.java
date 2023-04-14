package com.ktds.dsquare.board.qna.service;

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

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final AnswerResponse answerResponse;
    private final MemberRepository memberRepository;

    // 답변글 작성
    @Transactional
    public void createAnswer(Long qid, AnswerRequest dto) {
        Question question = questionRepository.findById(qid).orElseThrow(() -> new RuntimeException("Question not found"));
        Member writer = memberRepository.findById(dto.getWriterId()).orElseThrow(() -> new RuntimeException("Question not found"));
        Answer answer = new Answer();
        answer.setQuestion(question);
        answer.setId(dto.getId());
        answer.setWriter(writer);
        answer.setContent(dto.getContent());
        answer.setCreateDate(LocalDateTime.now());
        answer.setAtcId(dto.getAtcId());
        answer.setDeleteYn(false);
        answerRepository.save(answer);
    }

    // 답변글 조회
    public List<AnswerResponse> getAnswersByQuestion(Question qid) {
        List<AnswerResponse> answerResponses = new ArrayList<>();
        List<Answer> answers = answerRepository.findByQuestionAndDeleteYnOrderByCreateDateAsc(qid, false);
        for(Answer answer:answers){
//            Member member = memberRepository.findById(answer.getWriterId())
//                    .orElseThrow(() -> new RuntimeException("Member not found"));
//            MemberInfo writer = MemberInfo.toDto(member);
            answerResponses.add(AnswerResponse.toDto(answer, MemberInfo.toDto(answer.getWriter())));
        }
        return answerResponses;
    }


    // 답변글 수정
    @Transactional
    public void updateAnswer(Long aid, AnswerRequest request) {
        Answer answer = answerRepository.findById(aid).orElseThrow(() -> new RuntimeException("Answer does not exist"));
        answer.setContent(request.getContent());
        answer.setLastUpdateDate(LocalDateTime.now());
        answer.setAtcId(request.getAtcId());
    }

    // 답변글 삭제
    @Transactional
    public void deleteAnswer(Long aid) {
        Answer answer = answerRepository.findById(aid).orElseThrow(() -> new RuntimeException("Answer does not exist"));
        answer.setDeleteYn(true);
        answer.setLastUpdateDate(LocalDateTime.now());
    }

}
