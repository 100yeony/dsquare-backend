package com.ktds.dsquare.member;

import com.ktds.dsquare.member.dto.request.MemberUpdateRequest;
import com.ktds.dsquare.member.dto.request.SignupRequest;
import com.ktds.dsquare.member.team.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Builder
@Getter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String pw;
    @Column(unique = true, nullable = false)
    private String nickname;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String contact;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    private String ktMail;

    private Long activityScore;

    private LocalDateTime lastLoginDate;
    private LocalDateTime lastPwChangeDate;

    private String role;

    public List<String> getRole() {
        return List.of(role);
    }

    public void join(Team team) {
        this.team = team;
    }

    public void update(MemberUpdateRequest request) {
        if (request.getContact() != null)
            this.contact = request.getContact();
    }

    public static Member toEntity(SignupRequest dto) {
        return Member.builder()
                .email(dto.getEmail())
                .pw(dto.getPw())
                .nickname(dto.getNickname())
                .name(dto.getName())
                .contact(dto.getContact())
                .ktMail(dto.getKtMail())
                .activityScore(0L)
                .lastLoginDate(LocalDateTime.now())
                .lastPwChangeDate(LocalDateTime.now())
                .role("USER")
                .build();
    }

}
