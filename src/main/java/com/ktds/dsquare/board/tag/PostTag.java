package com.ktds.dsquare.board.tag;

import com.ktds.dsquare.board.Post;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PostTag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Post post;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Tag tag;


    public static PostTag toEntity(Post post, Tag tag) {
        return PostTag.builder()
                .post(post)
                .tag(tag)
                .build();
    }

}
