package com.ktds.dsquare.member.dto.response;

import com.ktds.dsquare.member.team.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class TeamInfo {

    private Long tid;
    private String name;

    public static TeamInfo toDto(Team entity){
        return TeamInfo.builder()
                .tid(entity.getTid())
                .name(entity.getName())
                .build();
    }
}
