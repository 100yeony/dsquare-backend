package com.ktds.dsquare.board.qna.dto.request;

import com.ktds.dsquare.common.file.dto.AttachmentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@NoArgsConstructor @AllArgsConstructor
public class AnswerRequest {

    private Long id;
//    private LocalDateTime createDate;
//    private LocalDateTime lastUpdateDate;
    private String content;
    private AttachmentDto attachment;
    private Boolean deleteYn;
    private Long qid;

}
