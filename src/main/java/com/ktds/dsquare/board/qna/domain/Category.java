package com.ktds.dsquare.board.qna.domain;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ktds.dsquare.member.Member;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

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


    @JsonBackReference //직렬화 X
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Member id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @JsonManagedReference
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private List<Category> childList;


}
