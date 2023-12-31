package com.ktds.dsquare.board.qna.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ktds.dsquare.common.file.dto.AttachmentDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class QuestionRequest {

    private Long qid;
    private Integer cid;
    private String title;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+9")
    private LocalDateTime createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+9")
    private LocalDateTime lastUpdateDate;
    private Long viewCnt;
    private AttachmentDto attachment;
    private Boolean deleteYn;
    private List<String> tags;

}
