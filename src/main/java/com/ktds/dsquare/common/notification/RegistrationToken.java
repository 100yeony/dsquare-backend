package com.ktds.dsquare.common.notification;

import com.ktds.dsquare.member.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RegistrationToken {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member owner;


    public static RegistrationToken toEntity(String value, Member owner) {
        return RegistrationToken.builder()
                .value(value)
                .owner(owner)
                .build();
    }

}
