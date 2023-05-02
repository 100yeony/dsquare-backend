package com.ktds.dsquare.common.dashboard.dto;

import com.ktds.dsquare.member.dto.response.MemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BestUserResponse {

    private MemberInfo memberInfo;
    private Long postCnt;

    public static BestUserResponse toDto(MemberInfo memberInfo, Long postCnt){
        return BestUserResponse.builder()
                .memberInfo(memberInfo)
                .postCnt(postCnt)
                .build();
    }
}
