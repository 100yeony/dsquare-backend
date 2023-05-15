package com.ktds.dsquare.board.tag;

import com.ktds.dsquare.board.carrot.Carrot;
import com.ktds.dsquare.board.qna.domain.Question;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 15)
    private String name;

    @OneToMany(mappedBy = "tag", orphanRemoval = true)
    private List<QuestionTag> questionTags;

    @OneToMany(mappedBy = "tag", orphanRemoval = true)
    private List<TalkTag> talkTags;

    @OneToMany(mappedBy = "tag", orphanRemoval = true)
    private List<CarrotTag> carrotTags;

    public static Tag toEntity(String name) {
        return Tag.builder()
                .name(name)
                .build();
    }
    public static List<Tag> toEntityList(List<String> names) {
        if (names == null)
            return List.of();

        return names.stream()
                .map(Tag::toEntity)
                .collect(Collectors.toList());
    }

    //궁금해요
    public List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        for (QuestionTag questionTag : this.questionTags) {
            questions.add((Question)questionTag.getPost());
        }
        return questions;
    }

    public void removeQuestion(Question question) {
        this.questionTags.remove(question);
    }


    //당근해요
    public List<Carrot> getCarrots() {
        List<Carrot> carrots = new ArrayList<>();
        for (CarrotTag carrotTag : this.carrotTags) {
            carrots.add(carrotTag.getCarrot());
        }
        return carrots;
    }

    public void removeCarrot(Carrot carrot) {
        this.carrotTags.remove(carrot);
    }

}
