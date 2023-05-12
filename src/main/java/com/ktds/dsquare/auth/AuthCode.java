package com.ktds.dsquare.auth;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Table(name = "AUTHENTICATION_CODE")
@Entity
public class AuthCode {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String account;
    @Column(nullable = false)
    private String code;

    @Column(updatable = false)
    @ColumnDefault("now() + '15 day'::interval")
    private LocalDateTime dueDate;


    public static AuthCode toEntity(String account, String code) {
        return AuthCode.builder()
                .account(account)
                .code(code)
                .build();
    }

}
