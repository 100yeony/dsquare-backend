package com.ktds.dsquare.board.qna.dto;

import com.ktds.dsquare.board.qna.domain.Category;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.member.dto.response.MemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
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
    private Long answerCnt;
    private Boolean managerAnswerYn;
    private Boolean atcYn;


    public static BriefQuestionResponse toDto(Question question, MemberInfo writerInfo, Category category, Long answerCnt, Boolean managerAnswerYn){
        return BriefQuestionResponse.builder()
                .qid(question.getQid())
                .writerInfo(writerInfo)
                .title(question.getTitle())
                .category(category)
                .content(question.getContent())
                .createDate(LocalDateTime.now())
                .viewCnt(question.getViewCnt())
                .atcYn(question.getAtcId()!=null)
                .answerCnt(answerCnt)
                .managerAnswerYn(managerAnswerYn)
                .build();
    }
}

// q1, 2 -> cid 4 -> 담당자 번호 1 -> 답변 쓴 사람 리스트:
// q3 ~ 5 -> cid 2 -> 담당자 번호 null -> 답변 쓴 사람 리스트:
