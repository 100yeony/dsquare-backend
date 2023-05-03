package com.ktds.dsquare.board.tag;

import com.ktds.dsquare.board.carrot.Carrot;
import com.ktds.dsquare.board.qna.domain.Question;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

//    위클리 핫토픽의 기준으로 사용할 필드
//    private Long cnt;

    @OneToMany(mappedBy = "tag", orphanRemoval = true)
    private List<QuestionTag> questionTags;

    @OneToMany(mappedBy = "tag", orphanRemoval = true)
    private List<CarrotTag> carrotTags;

    public static Tag toEntity(String name) {
        return Tag.builder()
                .name(name)
                .build();
    }

    //궁금해요
    public List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        for (QuestionTag questionTag : this.questionTags) {
            questions.add(questionTag.getQuestion());
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
