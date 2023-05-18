package com.ktds.dsquare.board.qna.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.tag.QuestionTag;
import com.ktds.dsquare.common.file.dto.AttachmentDto;
import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
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
    private BriefMemberInfo writerInfo;
    private CategoryResponse category;
    private String title;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+9")
    private LocalDateTime createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+9")
    private LocalDateTime lastUpdateDate;
    private Long viewCnt;
    private AttachmentDto attachment;
    private List<String> tags;
    private Long likeCnt;
    private Boolean likeYn;
    private Long commentCnt;

    public static QuestionResponse toDto(Question question,
                                         CategoryResponse category, Long likeCnt, Boolean likeYn, Long commentCnt){
        List<QuestionTag> questionTags = question.getQuestionTags();
        List<String> tags = new ArrayList<>();
        for(QuestionTag questionTag : questionTags)
            tags.add(questionTag.getTag().getName());

        return QuestionResponse.builder()
                .qid(question.getId())
                .writerInfo(BriefMemberInfo.toDto(question.getWriter()))
                .category(category)
                .title(question.getTitle())
                .content(question.getContent())
                .createDate(question.getCreateDate())
                .lastUpdateDate(question.getLastUpdateDate())
                .viewCnt(question.getViewCnt())
                .attachment(AttachmentDto.toDto(question.getAttachment()))
                .likeCnt(likeCnt)
                .likeYn(likeYn)
                .commentCnt(commentCnt)
                .tags(tags)
                .build();
    }

}
