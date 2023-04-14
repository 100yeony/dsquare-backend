package com.ktds.dsquare.board.qna.dto;

import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.member.dto.response.MemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class AnswerResponse {
    private Long aid;
    private MemberInfo writerInfo;
    private LocalDateTime createDate;
    private String content;
    private Long atcId;
    private Long qid;

    public static AnswerResponse toDto(Answer answer, MemberInfo writerInfo){
        Question q = answer.getQuestion();
        Long qid = q.getQid();
        return AnswerResponse.builder()
                .aid(answer.getId())
                .writerInfo(writerInfo)
                .content(answer.getContent())
                .createDate(LocalDateTime.now())
                .atcId(answer.getAtcId())
                .qid(qid)
                .build();
    }

}
