package com.ktds.dsquare.common.dashboard;

import com.ktds.dsquare.board.qna.repository.AnswerRepository;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import com.ktds.dsquare.common.dashboard.dto.BestUserResponse;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.MemberRepository;
import com.ktds.dsquare.member.dto.response.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;

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
            bestUsers.add(BestUserResponse.toDto(MemberInfo.toDto(mem.orElse(null)), postCnt.longValue()));
        }
        return bestUsers;
    }
}