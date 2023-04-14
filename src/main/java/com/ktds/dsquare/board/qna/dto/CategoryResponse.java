package com.ktds.dsquare.board.qna.dto;

import com.ktds.dsquare.board.qna.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class CategoryResponse {
    private Integer cid;
    private String name;
    private Long managerId;
//    private Category upCategory;
    private List<String> categoryHierarchy;
    public static CategoryResponse toDto(Category category){


        return CategoryResponse.builder()
                .cid(category.getCid())
                .name(category.getName())
                .managerId(category.getManagerId())
                .categoryHierarchy(category.getcategoryHierarchy())
                .build();
    }
}
