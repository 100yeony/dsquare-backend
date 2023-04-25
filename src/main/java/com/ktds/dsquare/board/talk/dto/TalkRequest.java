package com.ktds.dsquare.board.talk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TalkRequest {
    private Long writerId;
    private String title;
    private String content;
    private List<String> tags;

}
