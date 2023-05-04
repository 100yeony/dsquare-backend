package com.ktds.dsquare.board.qna.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.tag.QuestionTag;
import com.ktds.dsquare.common.file.dto.AttachmentDto;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime lastUpdateDate;
    private Long viewCnt;
    private AttachmentDto attachment;
    private List<String> tags;
    private Long likeCnt;
    private Boolean likeYn;
    private Long commentCnt;

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
