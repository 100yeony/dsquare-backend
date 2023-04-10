package com.ktds.dsquare.member;

import com.ktds.dsquare.member.dto.request.SignupRequest;
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
@Table(name = "MBR_BAS")
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String pw;
    @Column(unique = true, nullable = false)
    private String nickname;

    private String ktMail;
    @Column(columnDefinition = "CHAR", nullable = false)
    private String contact;

    private Long activityScore;

    private LocalDateTime lastLoginDate;
    private LocalDateTime lastPwChangeDate;

    private String role;

    public List<String> getRole() {
        return List.of(role);
    }


    public static Member toEntity(SignupRequest dto) {
        return Member.builder()
                .email(dto.getEmail())
                .pw(dto.getPw())
                .nickname(dto.getNickname())
                .ktMail(dto.getKtMail())
                .contact(dto.getContact())
                .activityScore(0L)
                .lastLoginDate(LocalDateTime.now())
                .role("USER")
                .build();
    }

}
