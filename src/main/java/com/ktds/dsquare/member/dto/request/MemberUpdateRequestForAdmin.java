package com.ktds.dsquare.member.dto.request;

import com.ktds.dsquare.member.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter @Getter
public class MemberUpdateRequestForAdmin {
    private Set<Role> role;

}
