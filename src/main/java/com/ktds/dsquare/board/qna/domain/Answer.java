package com.ktds.dsquare.board.qna.domain;

import com.ktds.dsquare.member.dto.request.SignupRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Builder
@Getter
public class Answer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private Long question_id;
    @Column(nullable = false)
    private Long writer_id;
    private LocalDateTime create_date;
    private LocalDateTime last_update_date;
    @Column(columnDefinition = "TEXT")
    private String content;
    private Long atc_id;
    @Column(nullable = false)
    private boolean delete_yn;



    public static Answer toEntity(SignupRequest dto) {
        return Answer.builder()
                .id(dto.getId())
                .question_id(dto.getQuestionId())
                .writer_id(dto.getWriterName())
                .create_date(LocalDateTime.now())
                .last_update_date(LocalDateTime.now())
                .content()
                .atc_id()
                .build();
    }

}


