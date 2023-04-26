package com.ktds.dsquare.board.carrot.dto;

import com.ktds.dsquare.board.carrot.Carrot;
import com.ktds.dsquare.member.Member;
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
public class CarrotResponse {

    private Long carrotId;
    private MemberInfo writerInfo;
    private String title;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private Long viewCnt;
    private Long likeCnt;
    private Boolean likeYn;
    private Long commentCnt;

    public static CarrotResponse toDto(Carrot entity, Member writer, Long likeCnt, Boolean likeYn, Long commentCnt) {
        MemberInfo writerInfo = MemberInfo.toDto(writer);
        return CarrotResponse.builder()
                .carrotId(entity.getId())
                .writerInfo(writerInfo)
                .title(entity.getTitle())
                .content(entity.getContent())
                .createDate(entity.getCreateDate())
                .lastUpdateDate(entity.getLastUpdateDate())
                .viewCnt(entity.getViewCnt())
                .likeCnt(likeCnt)
                .likeYn(likeYn)
                .commentCnt(commentCnt)
                .build();
    }

}
