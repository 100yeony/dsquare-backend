package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.qna.domain.Category;
import com.ktds.dsquare.board.qna.dto.CategoryResponse;
import com.ktds.dsquare.board.qna.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryResponse categoryResponse;

    // 카테고리 목록 조회
    public List<CategoryResponse> getAllCategories() {

        List<CategoryResponse> categoryResponses = new ArrayList<>();

        List<Category> categoryList = categoryRepository.findAll();
        for(Category c:categoryList){
            System.out.println("======>"+c);
            categoryResponses.add(categoryResponse.toDto(c));
        }
        return categoryResponses;
    }

}
