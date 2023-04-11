package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.qna.domain.Category;
import com.ktds.dsquare.board.qna.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    // 답변글 조회
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

}
