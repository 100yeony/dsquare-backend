package com.ktds.dsquare.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class DelMember {

    @Id
    private Long id;
    private LocalDateTime deleteAccountDate;

    public static DelMember toEntity(Long id) {
        return DelMember.builder()
                .id(id)
                .deleteAccountDate(LocalDateTime.now())
                .build();
    }

}
