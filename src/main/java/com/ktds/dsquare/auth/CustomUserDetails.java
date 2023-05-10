package com.ktds.dsquare.auth;

import com.ktds.dsquare.member.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
@Getter // Consider usage
public class CustomUserDetails implements UserDetails {

    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        member.getRole().forEach(role -> authorities.add(role::toString));
        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPw();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    /**
     * Authentication Failed (사용자 계정의 유효 기간이 만료 되었습니다.)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Authentication Failed (사용자 계정이 잠겨 있습니다.)
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Authentication Failed (자격 증명 유효 기간이 만료되었습니다.)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        LocalDate deadline = LocalDate.now().minusDays(90);
        return deadline.isBefore(member.getLastPwChangeDate().toLocalDate());
    }

    /**
     * Authentication Failed (유효하지 않은 사용자입니다.)
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}
