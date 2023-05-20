package com.ktds.dsquare.board.comment;

import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long>{
    //전체조회 관련
    Page<Comment> findByBoardTypeAndPostId(BoardType boardType, Long postId, Pageable pageable);

    //원글 삭제 시 댓글 삭제 관련
    List<Comment> findByBoardTypeAndPostId(BoardType boardType, Long postId);
    Long countByBoardTypeAndPostId(BoardType boardType, Long postId);

    //마이페이지 관련
    Page<Comment> findByWriter(Member writer, Pageable pageable);

    long countByPostId(long id);

}
