package com.ktds.dsquare.board.talk.dto;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TalkRegisterRequest {
    @ApiParam(value = "Talk title", required = true, example = "소통해요 제목입니다.")
    private String title;
    @ApiParam(value = "Talk content", required = true, example = "소통해요 내용입니다.")
    private String content;
    @ApiParam(value = "Talk tags", required = true, example = "['태그1', '태그2', '태그3']")
    private List<String> tags;

}
