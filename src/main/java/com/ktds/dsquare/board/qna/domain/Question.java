package com.ktds.dsquare.board.qna.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ktds.dsquare.member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Question {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qid;

    @Column(nullable = false)
    private Long writerId;

//    @Column
//    private String nickname;

    @ManyToOne
    @JoinColumn(name = "cid")
    private Category cid;

    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createDate;       // now로 설정
    private LocalDateTime lastUpdateDate;

    @Column(nullable = false)
    private Long viewCnt;       // 기본값 0

    private Long atcId;
    @Column(nullable = false)
    private Boolean deleteYn;       // 기본값 false

    /*
        cascade = CascadeType.REMOVE: 질문을 삭제하면 답변도 모두 함께 삭제되는 옵션
     */
    @JsonManagedReference //직렬화
    @OneToMany(mappedBy = "qid", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    @JsonBackReference //직렬화 X
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Member id;


    //검색 기능 관련
    public Integer getCid(){
        if(cid == null){
            return null;
        }
        return cid.getCid();
    }

}
