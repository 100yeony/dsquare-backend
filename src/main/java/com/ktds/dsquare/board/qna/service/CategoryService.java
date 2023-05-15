package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.qna.domain.Category;
import com.ktds.dsquare.board.qna.dto.response.BriefCategoryResponse;
import com.ktds.dsquare.board.qna.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // 카테고리 목록 조회
    public List<BriefCategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findByUpCategory(null);
        List<BriefCategoryResponse> briefCategoryResponses = new ArrayList<>();
        for(Category c : categories){
            if(!c.getDeleteYn())
                briefCategoryResponses.add(BriefCategoryResponse.toDto(c));
        }
        return briefCategoryResponses;
    }

}
