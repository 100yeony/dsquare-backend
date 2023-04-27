package com.ktds.dsquare.board.carrot.dto;

import com.ktds.dsquare.board.carrot.Carrot;
import com.ktds.dsquare.board.tag.CarrotTag;
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
public class BriefCarrotResponse {

    private Long carrotId;
    private MemberInfo writerInfo;
    private String title;
    private String content;
    private LocalDateTime createDate;
    private Long viewCnt;
    private List<String> tags;
    private Long likeCnt;
    private Boolean likeYn;
    private Long commentCnt;

    public static BriefCarrotResponse toDto(Carrot entity, MemberInfo writerInfo, Long likeCnt, Boolean likeYn, Long commentCnt) {
        List<CarrotTag> carrotTags = entity.getCarrotTags();
        List<String> tags = new ArrayList<>();
        for(CarrotTag carrotTag : carrotTags)
            tags.add(carrotTag.getTag().getName());

        return BriefCarrotResponse.builder()
                .carrotId(entity.getId())
                .writerInfo(writerInfo)
                .title(entity.getTitle())
                .content(entity.getContent())
                .createDate(entity.getCreateDate())
                .viewCnt(entity.getViewCnt())
                .tags(tags)
                .likeCnt(likeCnt)
                .likeYn(likeYn)
                .commentCnt(commentCnt)
                .build();
    }


}
