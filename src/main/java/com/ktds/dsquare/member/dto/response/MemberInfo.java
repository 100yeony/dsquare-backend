package com.ktds.dsquare.member.dto.response;

import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@NoArgsConstructor @AllArgsConstructor
@Builder
@Getter
public class MemberInfo {

    private Long id;
    private String email;
    private String ktMail;
    private String nickname;
    private String name;
    private String contact;
    private List<String> teamHierarchy;
    private String profileImage;
    private Set<Role> role;


    public static MemberInfo toDto(Member entity) {
        return MemberInfo.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .ktMail(entity.getKtMail())
                .nickname(entity.getNickname())
                .name(entity.getName())
                .contact(entity.getContact())
                .teamHierarchy(entity.getTeam().getTeamHierarchy())
                .profileImage(entity.getProfileImage())
                .role(entity.getRole())
                .build();
    }

}
