package com.ktds.dsquare.board.tag.repository;

import com.ktds.dsquare.board.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag,Long> {
    Tag findByName(String name);
}
