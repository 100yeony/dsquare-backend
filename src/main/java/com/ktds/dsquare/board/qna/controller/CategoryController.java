package com.ktds.dsquare.board.qna.controller;

import com.ktds.dsquare.board.qna.domain.Category;
import com.ktds.dsquare.board.qna.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    // 카테고리 목록 조회
    @GetMapping("/board/categories")
    public List<Category> getAllQuestions(){
        return categoryService.getAllCategories();
    }

}
