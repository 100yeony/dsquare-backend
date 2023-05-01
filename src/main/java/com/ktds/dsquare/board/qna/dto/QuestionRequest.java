package com.ktds.dsquare.board.qna.dto;

import com.ktds.dsquare.common.file.dto.AttachmentDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class QuestionRequest {

    private Long qid;
    private Long writerId;
    private Integer cid;
    private String title;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private Long viewCnt;
    private AttachmentDto attachment;
    private Boolean deleteYn;
    private List<String> tags;

}
