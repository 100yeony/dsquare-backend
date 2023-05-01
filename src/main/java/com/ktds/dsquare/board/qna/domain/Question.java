package com.ktds.dsquare.board.qna.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ktds.dsquare.board.qna.dto.QuestionRequest;
import com.ktds.dsquare.common.file.Attachment;
import com.ktds.dsquare.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Question {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qid;

    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;

    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createDate;       // now로 설정
    private LocalDateTime lastUpdateDate;

    @Column(nullable = false)
    private Long viewCnt;       // 기본값 0

    @OneToOne(mappedBy = "post", fetch = FetchType.LAZY)
    private Attachment attachment;
    @Column(nullable = false)
    private Boolean deleteYn;       // 기본값 false

    @JsonBackReference //직렬화 X
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private Member writer;

    @OneToMany(mappedBy = "question")
    private List<QuestionTag> questionTags = new ArrayList<>();

    public static Question toEntity(QuestionRequest dto, Member writer, Category category){
        LocalDateTime now = LocalDateTime.now();
        return Question.builder()
                .writer(writer)
                .title(dto.getTitle())
                .content(dto.getContent())
                .createDate(now)
                .viewCnt(0L)
                .deleteYn(false)
                .category(category)
                .build();
    }

    public void registerAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public void updateQuestion(String title, String content, Category category){
        LocalDateTime now = LocalDateTime.now();
        this.title = title;
        this.content = content;
        this.lastUpdateDate = now;
        this.category = category;
    }

    public void deleteQuestion(){
        this.deleteYn = true;
    }

    public void increaseViewCnt() {
        this.viewCnt += 1;
    }

}
