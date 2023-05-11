package com.ktds.dsquare.auth;

import com.ktds.dsquare.auth.jwt.JwtProperties;
import com.ktds.dsquare.member.Member;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Map;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class AuthToken {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(columnDefinition = "TEXT") //@Lob
    private String accessToken;
    @Column(columnDefinition = "TEXT") //@Lob
    private String refreshToken;


    public static AuthToken toEntity(UserDetails principal, Map<String, String> tokens) {
        AuthTokenBuilder builder = AuthToken.builder();
        if (principal instanceof CustomUserDetails)
            builder.member(((CustomUserDetails)principal).getMember());

        return builder
                .accessToken(tokens.get(JwtProperties.ACCESS_KEY()))
                .refreshToken(tokens.get(JwtProperties.REFRESH_KEY()))
                .build();
    }

    public void refresh(Map<String, String> freshTokens) {
        accessToken = freshTokens.get(JwtProperties.ACCESS_KEY());
        refreshToken = freshTokens.get(JwtProperties.REFRESH_KEY());
    }

}
