package com.ktds.dsquare.board.qna.repository;

import com.ktds.dsquare.board.qna.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository  extends JpaRepository<Category,Integer> {
}
