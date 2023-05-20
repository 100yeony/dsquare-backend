package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.qna.domain.Category;
import com.ktds.dsquare.board.qna.dto.response.BriefCategoryResponse;
import com.ktds.dsquare.board.qna.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

    public Category getCategory(int cid) {
        return categoryRepository.findById(cid).orElseThrow(EntityNotFoundException::new);
    }

    public List<Category> getWorkCategories() {
        Category workCategory = categoryRepository.findByName("업무")
                .orElseThrow(() -> new EntityNotFoundException("업무 범주가 존재하지 않습니다."));
        return getLowerCategories(workCategory);
    }
    public List<Category> getNonWorkCategories() {
        Category nonWorkCategory = categoryRepository.findByName("비업무")
                .orElseThrow(() -> new EntityNotFoundException("비업무 범주가 존재하지 않습니다."));
        return getLowerCategories(nonWorkCategory);
    }
    public List<Category> getLowerCategories(Category category) {
        Assert.notNull(category, "Cannot scan lower categories of null.");

        Queue<Category> categoryQueue = new LinkedList<>();
        List<Category> categories = new ArrayList<>();

        categoryQueue.offer(category);
        while (!categoryQueue.isEmpty()) {
            Category cur = categoryQueue.poll();
            categories.add(cur);

            for (Category lower : cur.getChildList())
                categoryQueue.offer(lower);
        }
        return categories;
    }

}
