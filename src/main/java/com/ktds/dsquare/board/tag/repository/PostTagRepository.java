package com.ktds.dsquare.board.tag.repository;

import com.ktds.dsquare.board.tag.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, Long> {
}
