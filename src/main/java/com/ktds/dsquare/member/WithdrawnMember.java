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
public class WithdrawnMember {

    @Id
    private Long id;

    public static WithdrawnMember toEntity(Long id) {
        return WithdrawnMember.builder()
                .id(id)
                .build();
    }

}
