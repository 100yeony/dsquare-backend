package com.ktds.dsquare.board.qna.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ktds.dsquare.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @Column(nullable = false)
    private Integer cid;

    @Column(nullable = false, length = 20)  // unique = true 넣어야할까?
    private String name;

    @Column(length = 2)
    private String upId;

    @Column(nullable = false)
    private Integer depth;

    @JsonManagedReference //직렬화
    @OneToOne(mappedBy = "cid")
    private Member mid;
}
