package com.ktds.dsquare.board.dashboard.dto;

import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BestUserResponse {

    private BriefMemberInfo memberInfo;
    private Long postCnt;

    public static BestUserResponse toDto(BriefMemberInfo memberInfo, Long postCnt){
        return BestUserResponse.builder()
                .memberInfo(memberInfo)
                .postCnt(postCnt)
                .build();
    }
}
