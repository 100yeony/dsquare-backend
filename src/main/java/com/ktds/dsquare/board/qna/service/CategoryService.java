package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.qna.domain.Category;
import com.ktds.dsquare.board.qna.dto.BriefCategoryResponse;
import com.ktds.dsquare.board.qna.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final BriefCategoryResponse briefCategoryResponse;

    // 카테고리 목록 조회
    public List<BriefCategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findByUpCategory(null);
        List<BriefCategoryResponse> briefCategoryResponses = new ArrayList<>();
        for(Category c : categories){
            briefCategoryResponses.add(BriefCategoryResponse.toDto(c));
        }
        return briefCategoryResponses;
    }

}
