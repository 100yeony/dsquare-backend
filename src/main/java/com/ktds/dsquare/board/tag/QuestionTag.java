package com.ktds.dsquare.board.tag;

import com.ktds.dsquare.board.Post;
import com.ktds.dsquare.board.qna.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "question_tag")
public class QuestionTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
//    @JoinColumn(name = "question_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    private LocalDateTime createDate;

    public static QuestionTag toEntity(Question question, Tag tag) {
        return QuestionTag.builder()
                .post(question)
                .tag(tag)
                .createDate(LocalDateTime.now())
                .build();
    }

}
