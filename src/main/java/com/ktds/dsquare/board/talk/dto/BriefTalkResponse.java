package com.ktds.dsquare.board.talk.dto;

import com.ktds.dsquare.board.tag.TalkTag;
import com.ktds.dsquare.board.talk.Talk;
import com.ktds.dsquare.member.dto.response.MemberInfo;
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
public class BriefTalkResponse {

    private Long talkId;
    private MemberInfo writerInfo;
    private String title;
    private String content;
    private LocalDateTime createDate;
    private Long viewCnt;
    private Long commentCnt;
    private Integer likeCnt;
    private Boolean likeYn;
    private List<String> tags;

    public static BriefTalkResponse toDto(Talk talk, Long commentCnt, Integer likeCnt, Boolean likeYn) {
        List<TalkTag> talkTags = talk.getTalkTags();
        List<String> tags = new ArrayList<>();
        for(TalkTag talkTag : talkTags)
            tags.add(talkTag.getTag().getName());

        return BriefTalkResponse.builder()
                .talkId(talk.getId())
                .writerInfo(MemberInfo.toDto(talk.getWriter()))
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
