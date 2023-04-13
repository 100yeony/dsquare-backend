package com.ktds.dsquare.board.qna.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ktds.dsquare.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @Column(nullable = false)
    private Integer cid;

    @Column(unique = true, nullable = false, length = 20)
    private String name;

    @JsonBackReference //직렬화 X
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member")
    private Member member;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Category upCategory;

//    @OneToMany(mappedBy = "upCategory", fetch = FetchType.LAZY)
//    private List<Category> childList;

    public List<String> getcategoryHierarchy() {
        Deque<String> nameQueue = new ArrayDeque<>();
        for (Category category = this; category != null; category = category.getUpCategory())
            nameQueue.offerFirst(category.getName());

        return new ArrayList<>(nameQueue);
    }
}
