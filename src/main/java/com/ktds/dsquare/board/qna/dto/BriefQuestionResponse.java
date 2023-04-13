package com.ktds.dsquare.board.qna.dto;

import com.ktds.dsquare.board.qna.domain.Category;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.member.dto.response.MemberInfo;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BriefQuestionResponse {

    private Long qid;
    private MemberInfo writerInfo;
    private Category category;
    private String title;
    private String content;
    private LocalDateTime createDate; //정렬 기준
    private Long viewCnt;
    private Integer answerCnt;
    private Boolean managerAnswerYn;
    private Boolean atcYn;


    public static QuestionResponse toDto(Question question, MemberInfo writerInfo){
        return QuestionResponse.builder()
                .qid(question.getQid())
                .writerInfo(writerInfo)
                .title(question.getTitle())
                .content(question.getContent())
                .createDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
                .viewCnt(question.getViewCnt())
                .atcId(question.getAtcId())
                .build();
    }
}

