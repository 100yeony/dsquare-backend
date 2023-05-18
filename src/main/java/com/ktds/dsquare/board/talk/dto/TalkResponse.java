package com.ktds.dsquare.board.talk.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ktds.dsquare.board.tag.TalkTag;
import com.ktds.dsquare.board.talk.Talk;
import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TalkResponse {

    @ApiModelProperty(notes="Talk ID", example = "0", required = true)
    private Long talkId;
    @ApiModelProperty(notes="작성자 정보", example = "writer", required = true)
    private BriefMemberInfo writerInfo;
    @ApiModelProperty(notes="Talk title", example = "소통해요 제목입니다.", required = true)
    private String title;
    @ApiModelProperty(notes="Talk content", example = "소통해요 내용입니다.", required = true)
    private String content;
    @ApiModelProperty(notes="작성일 정보", example = "2023-04-26", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+9")
    private LocalDateTime createDate;
    @ApiModelProperty(notes="Talk view count", example = "31", required = true)
    private Long viewCnt;
    @ApiModelProperty(notes="Talk comment count", example = "2", required = true)
    private Long commentCnt;
    @ApiModelProperty(notes="Talk like count", example = "5", required = true)
    private Long likeCnt;
    @ApiModelProperty(notes="if current user liked this Post", example = "false", required = true)
    private Boolean likeYn;
    @ApiModelProperty(notes="Talk tags", example = "['aa', 'bb', 'cc']", required = true)
    private List<String> tags;

    public static TalkResponse toDto(Talk talk, Long commentCnt, Long likeCnt, Boolean likeYn) {
        List<TalkTag> talkTags = talk.getTalkTags();
        List<String> tags = new ArrayList<>();
        for(TalkTag talkTag : talkTags)
            tags.add(talkTag.getTag().getName());

        return TalkResponse.builder()
                .talkId(talk.getId())
                .writerInfo(BriefMemberInfo.toDto(talk.getWriter()))
                .title(talk.getTitle())
                .content(talk.getContent())
                .createDate(talk.getCreateDate())
                .viewCnt(talk.getViewCnt())
                .commentCnt(commentCnt)
                .likeCnt(likeCnt)
                .likeYn(likeYn)
                .tags(tags)
                .build();
    }
}
