package com.ktds.dsquare.board.qna.dto;

import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.domain.QuestionTag;
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
public class QuestionResponse {

    private Long qid;
    private MemberInfo writerInfo;
    private CategoryResponse category;
    private String title;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private Long viewCnt;
    private Long commentCnt;
    private Long atcId;
    private List<String> tags;
    private Long likeCnt;
    private Boolean likeYn;

    public static QuestionResponse toDto(Question question, MemberInfo writerInfo,
                                         CategoryResponse category, Long likeCnt, Boolean likeYn, Long commentCnt){
        List<QuestionTag> questionTags = question.getQuestionTags();
        List<String> tags = new ArrayList<>();
        for(QuestionTag questionTag : questionTags)
            tags.add(questionTag.getTag().getName());

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
                .tags(tags)
                .likeCnt(likeCnt)
                .likeYn(likeYn)
                .commentCnt(commentCnt)
                .build();
    }

}
