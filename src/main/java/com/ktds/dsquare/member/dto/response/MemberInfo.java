package com.ktds.dsquare.member.dto.response;

import com.ktds.dsquare.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@Builder
@Getter
public class MemberInfo {

    private Long id;
    private String email;
    private String nickname;
    private String name;
    private String contact;
    private List<String> teamHierarchy;


    public static MemberInfo toDto(Member entity) {
        return MemberInfo.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .name(entity.getName())
                .contact(entity.getContact())
                .teamHierarchy(entity.getTeam().getTeamHierarchy())
                .build();
    }

}
