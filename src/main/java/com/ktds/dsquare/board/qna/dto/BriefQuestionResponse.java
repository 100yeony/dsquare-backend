package com.ktds.dsquare.board.qna.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.tag.QuestionTag;
import com.ktds.dsquare.member.dto.response.MemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BriefQuestionResponse {

    private Long qid;
    private MemberInfo writerInfo;
    private CategoryResponse category;
    private String title;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createDate; //정렬 기준
    private Long viewCnt;
    private Long answerCnt;
    private Boolean managerAnswerYn;
    private Boolean atcYn;
    private Long likeCnt;
    private Boolean likeYn;
    private Long commentCnt;
    private List<String> tags;

    public static BriefQuestionResponse toDto(Question question, MemberInfo writerInfo, CategoryResponse category, Long answerCnt,
                                              Boolean managerAnswerYn, Long likeCnt, Boolean likeYn, Long commentCnt){
        List<QuestionTag> questionTags = question.getQuestionTags();
        List<String> tags = new ArrayList<>();
        for(QuestionTag questionTag : questionTags)
            tags.add(questionTag.getTag().getName());

        return BriefQuestionResponse.builder()
                .qid(question.getQid())
                .writerInfo(writerInfo)
                .title(question.getTitle())
                .category(category)
                .content(question.getContent())
                .createDate(question.getCreateDate())
                .viewCnt(question.getViewCnt())
                .atcYn(question.getAtcId()!=null)
                .answerCnt(answerCnt)
                .managerAnswerYn(managerAnswerYn)
                .likeCnt(likeCnt)
                .likeYn(likeYn)
                .commentCnt(commentCnt)
                .tags(tags)
                .build();
    }
}
