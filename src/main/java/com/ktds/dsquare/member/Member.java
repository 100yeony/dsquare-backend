package com.ktds.dsquare.member;

import com.ktds.dsquare.member.dto.request.SignupRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    private String ktMail;
    @Column(nullable = false)
    private String contact;

    private Long activityScore;

    private LocalDateTime lastLoginDate;
    private LocalDateTime lastPwChangeDate;


    public static Member toEntity(SignupRequest dto) {
        return Member.builder()
                .email(dto.getEmail())
                .pw(dto.getPw())
                .nickname(dto.getNickname())
                .ktMail(dto.getKtMail())
                .contact(dto.getContact())
                .activityScore(0L)
                .lastLoginDate(LocalDateTime.now())
                .build();
    }

}
