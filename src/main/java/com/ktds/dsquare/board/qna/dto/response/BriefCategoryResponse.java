package com.ktds.dsquare.board.qna.dto.response;

import com.ktds.dsquare.board.qna.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class BriefCategoryResponse {
    private Integer cid;
    private String name;
    private List<BriefCategoryResponse> childList;

    public static BriefCategoryResponse toDto(Category category){
        List<Category> childCategories = category.getChildList();
        List<BriefCategoryResponse> childResponses = new ArrayList<>();
        for (Category childCategory : childCategories) {
            childResponses.add(BriefCategoryResponse.toDto(childCategory));
        }

        return BriefCategoryResponse.builder()
                .cid(category.getCid())
                .name(category.getName())
                .childList(childResponses)
                .build();
    }
}
