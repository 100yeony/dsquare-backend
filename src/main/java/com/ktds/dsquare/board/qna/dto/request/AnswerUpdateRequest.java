package com.ktds.dsquare.board.qna.dto.request;

import com.ktds.dsquare.common.file.dto.AttachmentDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerUpdateRequest {

    private String content;
    private AttachmentDto attachment;

}
