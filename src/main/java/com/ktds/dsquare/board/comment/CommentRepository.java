package com.ktds.dsquare.board.comment;

import com.ktds.dsquare.board.enums.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long>{
    List<Comment> findByBoardTypeAndPostId(BoardType boardType, Long postId);
    Optional<Comment> findById(Long id);
    Long countByBoardTypeAndPostId(BoardType boardType, Long postId);
}
