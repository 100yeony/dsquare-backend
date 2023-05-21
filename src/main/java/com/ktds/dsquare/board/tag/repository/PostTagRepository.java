package com.ktds.dsquare.board.tag.repository;

import com.ktds.dsquare.board.Post;
import com.ktds.dsquare.board.tag.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    List<PostTag> findByPost(Post post);

}
