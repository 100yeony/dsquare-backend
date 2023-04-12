package com.ktds.dsquare.member.dto.response;

import com.ktds.dsquare.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor
@Builder
@Getter
public class BriefMemberInfo {

    private Long id;
    private String email;
    private String nickname;
    private String teamName;


    public static BriefMemberInfo toDto(Member entity) {
        return BriefMemberInfo.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .teamName(entity.getTeam().getName())
                .build();
    }

}
