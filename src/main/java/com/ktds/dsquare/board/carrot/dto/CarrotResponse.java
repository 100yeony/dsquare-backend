package com.ktds.dsquare.board.carrot.dto;

import com.ktds.dsquare.board.carrot.Carrot;
import com.ktds.dsquare.board.tag.CarrotTag;
import com.ktds.dsquare.board.tag.QuestionTag;
import com.ktds.dsquare.member.Member;
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
public class CarrotResponse {

    private Long carrotId;
    private MemberInfo writerInfo;
    private String title;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private Long viewCnt;
    private List<String> tags;
    private Long likeCnt;
    private Boolean likeYn;
    private Long commentCnt;

    public static CarrotResponse toDto(Carrot entity, Member writer, Long likeCnt, Boolean likeYn, Long commentCnt) {
        MemberInfo writerInfo = MemberInfo.toDto(writer);
        List<CarrotTag> carrotTags = entity.getCarrotTags();
        List<String> tags = new ArrayList<>();
        for(CarrotTag carrotTag : carrotTags)
            tags.add(carrotTag.getTag().getName());

        return CarrotResponse.builder()
                .carrotId(entity.getId())
                .writerInfo(writerInfo)
                .title(entity.getTitle())
                .content(entity.getContent())
                .createDate(entity.getCreateDate())
                .lastUpdateDate(entity.getLastUpdateDate())
                .viewCnt(entity.getViewCnt())
                .tags(tags)
                .likeCnt(likeCnt)
                .likeYn(likeYn)
                .commentCnt(commentCnt)
                .build();
    }

}
