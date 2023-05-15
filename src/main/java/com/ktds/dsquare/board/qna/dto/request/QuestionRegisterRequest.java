package com.ktds.dsquare.board.qna.dto.request;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class QuestionRegisterRequest {

    private Integer cid;
    private String title;
    private String content;
    private List<String> tags;

    // TODO regarded as useless
    private Long qid;
//    private AttachmentDto attachment;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private Long viewCnt;
    private Boolean deleteYn;
    // -------------------------

}
