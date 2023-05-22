package com.ktds.dsquare.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Service
public class PostSelectService {

    private final PostRepository postRepository;


    public Post selectWithId(long id) {
        return postRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

}
