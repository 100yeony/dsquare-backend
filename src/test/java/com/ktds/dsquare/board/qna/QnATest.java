package com.ktds.dsquare.board.qna;

import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class QnATest {

    @Autowired
    private QuestionRepository questionRepository;


//    @AfterEach
//    public void afterEach(){
//        answerRepositoryImpl.clearStore();
//    }

    @Test
    void testJpaQuestion(){
        Question q1 = new Question();
        q1.setId(1L);
        q1.setContent("질문의 내용입니다");
        q1.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q1);

        Question q2 = new Question();
        q2.setId(2L);
        q2.setContent("내용2");
        q2.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q2);
    }

//    @Test
//    void testJpaAnswer(){
//        Answer a1 = new Answer();
//        a1.setId(1L);
//        a1.setContent("답변의 내용입니다");
//        a1.setCreateDate(LocalDateTime.now());
//        this.answerRepository.save(a1);
//    }
}
