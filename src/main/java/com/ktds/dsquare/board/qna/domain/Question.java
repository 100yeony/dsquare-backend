package com.ktds.dsquare.board.qna.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ktds.dsquare.board.Post;
import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.board.qna.dto.QuestionRequest;
import com.ktds.dsquare.board.tag.QuestionTag;
import com.ktds.dsquare.common.file.Attachment;
import com.ktds.dsquare.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@DiscriminatorValue(BoardType.Constant.QUESTION)
@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
@DynamicInsert @DynamicUpdate
public class Question extends Post {

    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;

    @Column(nullable = false, length = 100)
    private String title;
    @Column(columnDefinition = "TEXT", nullable = false, length = 10000)
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

    @OneToMany(mappedBy = "post")
    private List<QuestionTag> questionTags;

    private Long likeCnt;

    public static Question toEntity(QuestionRequest dto, Member writer, Category category){
        LocalDateTime now = LocalDateTime.now();
        String name = BoardType.QUESTION.name;
        return Question.builder()
                .writer(writer)
                .title(dto.getTitle())
                .content(dto.getContent())
                .createDate(now.plusHours(9))
                .viewCnt(0L)
                .deleteYn(false)
                .category(category)
                .likeCnt(0L)
                .build();
    }

    public void updateQuestion(String title, String content, Category category){
        LocalDateTime now = LocalDateTime.now();
        this.title = title;
        this.content = content;
        this.lastUpdateDate = now.plusHours(9);
        this.category = category;
    }

    public void deleteQuestion(){
        this.deleteYn = true;
    }

    public void increaseViewCnt() { this.viewCnt += 1; }

    public void like() { this.likeCnt += 1; }

    public void cancleLike(){ this.likeCnt -= 1; }

}
