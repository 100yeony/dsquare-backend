package com.ktds.dsquare.board.qna.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerRegisterResponse {

    private Long aid;
    private Long qid;
    private BriefMemberInfo writerInfo;
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createDate;


    public static AnswerRegisterResponse toDto(Answer entity){
        return AnswerRegisterResponse.builder()
                .aid(entity.getId())
                .qid(entity.getQuestion().getId())
                .writerInfo(BriefMemberInfo.toDto(entity.getWriter()))
                .content(entity.getContent())
                .createDate(entity.getCreateDate())
                .build();
    }

}
