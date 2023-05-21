package com.ktds.dsquare.board.qna.dto.request;

import com.ktds.dsquare.common.file.dto.AttachmentDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionUpdateRequest {

    private int cid;
    private String title;
    private String content;
    private List<String> tags;
    private AttachmentDto attachment;

}
