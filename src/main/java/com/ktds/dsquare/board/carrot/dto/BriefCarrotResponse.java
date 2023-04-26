package com.ktds.dsquare.board.carrot.dto;

import com.ktds.dsquare.board.carrot.Carrot;
import com.ktds.dsquare.member.dto.response.MemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BriefCarrotResponse {

    private Long carrotId;
    private MemberInfo writerInfo;
    private String title;
    private String content;
    private LocalDateTime createDate;
    private Long viewCnt;
    private Long likeCnt;
    private Boolean likeYn;
    private Long commentCnt;

    public static BriefCarrotResponse toDto(Carrot entity, MemberInfo writerInfo, Long likeCnt, Boolean likeYn, Long commentCnt) {
        return BriefCarrotResponse.builder()
                .carrotId(entity.getId())
                .writerInfo(writerInfo)
                .title(entity.getTitle())
                .content(entity.getContent())
                .createDate(entity.getCreateDate())
                .viewCnt(entity.getViewCnt())
                .likeCnt(likeCnt)
                .likeYn(likeYn)
                .commentCnt(commentCnt)
                .build();
    }


}
