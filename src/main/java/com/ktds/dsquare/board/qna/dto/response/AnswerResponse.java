package com.ktds.dsquare.board.qna.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.common.file.dto.AttachmentDto;
import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
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
    private BriefMemberInfo writerInfo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+9")
    private LocalDateTime createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+9")
    private LocalDateTime lastUpdateDate;
    private String content;
    private AttachmentDto attachment;
    private Long qid;
    private Long likeCnt;
    private Boolean likeYn;
    private Long commentCnt;

    /*
    TODO refactor
    ...
     */
    public static AnswerResponse toDto(Answer answer, Long likeCnt, Boolean likeYn, Long commentCnt){
        Question q = answer.getQuestion();
        Long qid = q.getId();
        return AnswerResponse.builder()
                .aid(answer.getId())
                .writerInfo(BriefMemberInfo.toDto(answer.getWriter()))
                .content(answer.getContent())
                .createDate(answer.getCreateDate())
                .lastUpdateDate(answer.getLastUpdateDate())
                .attachment(AttachmentDto.toDto(answer.getAttachment()))
                .qid(qid)
                .likeCnt(likeCnt)
                .likeYn(likeYn)
                .commentCnt(commentCnt)
                .build();
    }

}
