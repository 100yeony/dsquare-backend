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
public class QuestionResponse {

    private Long qid;
    private MemberInfo writerInfo;
    private Category category;
    private String title;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private Long viewCnt;
    private Long atcId;


    public static QuestionResponse toDto(Question question, MemberInfo writerInfo, Category category){
        return QuestionResponse.builder()
                .qid(question.getQid())
                .writerInfo(writerInfo)
                .category(category)
                .title(question.getTitle())
                .content(question.getContent())
                .createDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
                .viewCnt(question.getViewCnt())
                .atcId(question.getAtcId())
                .build();
    }

/*    public Question getQuestion(){
        return this.question;
    }
    // Question 엔티티와 일대일로 매핑되는 필드
    private List<Answer> answers;
    public List<Answer> getAnswers() {
        return this.answers;
    }
    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }*/

}
