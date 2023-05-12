package com.ktds.dsquare.member.dto.request;

import com.ktds.dsquare.member.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter @Getter
public class MemberUpdateRequest {

    private String contact;
    private Long tid;
//    private String ktmail;
    private List<Role> role;

}
