package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Service
public class AnswerSelectService {

    private final AnswerRepository answerRepository;


    public Answer selectWithId(long id) {
        return answerRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

}
