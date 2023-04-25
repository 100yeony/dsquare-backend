package com.ktds.dsquare.board.qna.controller;

import com.ktds.dsquare.board.qna.dto.BriefCategoryResponse;
import com.ktds.dsquare.board.qna.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // 카테고리 목록 조회
    @GetMapping("/board/categories")
    public List<BriefCategoryResponse> getAllCategories(){
        return categoryService.getAllCategories();
    }

}
