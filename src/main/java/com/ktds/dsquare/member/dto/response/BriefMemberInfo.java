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
public class BriefMemberInfo {

    private Long id;
    private String email;
    private String nickname;
    private String name;
    private List<String> teamHierarchy;
    private String profileImage;


    public static BriefMemberInfo toDto(Member entity) {
        return BriefMemberInfo.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .name(entity.getName())
                .teamHierarchy(entity.getTeam().getTeamHierarchy())
                .profileImage(entity.getProfileImage())
                .build();
    }

}
