package com.ktds.dsquare.board.qna.dto;

import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionRegisterResponse {

    private long id;
    private CategoryResponse category;
    private BriefMemberInfo writerInfo;


    public static QuestionRegisterResponse toDto(Question entity) {
        return QuestionRegisterResponse.builder()
                .id(entity.getId())
                .category(CategoryResponse.toDto(entity.getCategory()))
                .writerInfo(BriefMemberInfo.toDto(entity.getWriter()))
                .build();
    }

}
