package com.ktds.dsquare.board.qna.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.common.file.dto.AttachmentDto;
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
    private AttachmentDto attachment;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+9")
    private LocalDateTime createDate;


    public static AnswerRegisterResponse toDto(Answer entity){
        return AnswerRegisterResponse.builder()
                .aid(entity.getId())
                .qid(entity.getQuestion().getId())
                .writerInfo(BriefMemberInfo.toDto(entity.getWriter()))
                .content(entity.getContent())
                .attachment(AttachmentDto.toDto(entity.getAttachment()))
                .createDate(entity.getCreateDate())
                .build();
    }

}
