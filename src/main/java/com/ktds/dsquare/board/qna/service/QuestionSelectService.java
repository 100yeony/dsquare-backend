package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Service
public class QuestionSelectService {

    private final QuestionRepository questionRepository;


    public Question selectWithId(long id) {
        return questionRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

}
