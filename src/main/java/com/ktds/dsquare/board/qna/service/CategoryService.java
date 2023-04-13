package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.qna.domain.Category;
import com.ktds.dsquare.board.qna.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // 카테고리 목록 조회
    public List<Category> getAllCategories() {
        return categoryRepository.findByUpCategory(null);
    }

}
