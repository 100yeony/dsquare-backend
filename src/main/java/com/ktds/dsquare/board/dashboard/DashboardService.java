package com.ktds.dsquare.board.dashboard;

import com.ktds.dsquare.board.dashboard.dto.BestUserResponse;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.BriefQuestionResponse;
import com.ktds.dsquare.board.qna.repository.AnswerRepository;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import com.ktds.dsquare.board.qna.service.QuestionService;
import com.ktds.dsquare.common.exception.PostNotFoundException;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.MemberRepository;
import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@PersistenceContext
public class DashboardService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;
    private final QuestionService questionService;
    private final EntityManager em;

    //read - 명예의 전당(궁금해요) 주간, 월간
    public List<BriefQuestionResponse> getHallOfFame(String key, Member user) {
        List<BriefQuestionResponse> bestQnaList = new ArrayList<>();
        LocalDateTime period;
        LocalDateTime now = LocalDateTime.now();

        if(key.equals("week")){
            period = now.plusHours(9).minusDays(7);
        } else if (key.equals("month")) {
            period = now.plusHours(9).minusMonths(1);
        } else {
            throw new IllegalArgumentException("Invalid key value");
        }

        String jpql = "SELECT q.id " +
                "FROM Question q " +
                "JOIN Category c on q.category = c.cid " +
                "JOIN Answer a on q.id = a.question " +
                "WHERE a.writer = c.manager " +
                "AND q.createDate >= :key " +
                "GROUP BY q.id " +
                "ORDER BY sum(a.likeCnt + q.likeCnt) desc";

        List<Long> resultList = em.createQuery(jpql)
                .setParameter("key", period)
                .setMaxResults(5)
                .getResultList();
        for (Long q : resultList) {
            Question question = questionRepository.findById(q)
                    .orElseThrow(() -> new PostNotFoundException());
            bestQnaList.add(questionService.makeBriefQuestionRes(question, user));
        }
        return bestQnaList;
    }


    //read - 질문왕 & 답변왕
    public List<BestUserResponse> getAllBestUsers(String key) {
        List<BestUserResponse> bestUsers = new ArrayList<>();
        List<Object[]> resultList = new ArrayList<>();
        if (key.equals("question")) {
            resultList = questionRepository.findBestUser(5);
        } else if (key.equals("answer")) {
            resultList = answerRepository.findBestUser(5);
        }
        for (Object[] R : resultList) {
            BigInteger writerId = (BigInteger) R[0];
            BigInteger postCnt = (BigInteger) R[1];
            Optional<Member> mem = memberRepository.findById(writerId.longValue());
            bestUsers.add(BestUserResponse.toDto(BriefMemberInfo.toDto(mem.orElse(null)), postCnt.longValue()));
        }
        return bestUsers;
    }
}