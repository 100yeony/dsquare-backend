package com.ktds.dsquare.board.qna.repository;

import com.ktds.dsquare.board.qna.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Optional;

@Repository
public interface CategoryRepository  extends JpaRepository<Category,Integer> {
    Optional<Category> findByCid(Integer cid);
    List<Category> findByUpCategory(Category upCategory);

}
