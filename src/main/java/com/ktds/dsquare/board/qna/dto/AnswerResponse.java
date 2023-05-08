package com.ktds.dsquare.board.qna.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime lastUpdateDate;
    private String content;
    private Long atcId;
    private Long qid;
    private Long likeCnt;
    private Boolean likeYn;
    private Long commentCnt;

    public static AnswerResponse toDto(Answer answer, MemberInfo writerInfo, Long likeCnt, Boolean likeYn, Long commentCnt){
        Question q = answer.getQuestion();
        Long qid = q.getId();
        return AnswerResponse.builder()
                .aid(answer.getId())
                .writerInfo(writerInfo)
                .content(answer.getContent())
                .createDate(answer.getCreateDate())
                .lastUpdateDate(answer.getLastUpdateDate())
                .atcId(answer.getAtcId())
                .qid(qid)
                .likeCnt(likeCnt)
                .likeYn(likeYn)
                .commentCnt(commentCnt)
                .build();
    }

}
