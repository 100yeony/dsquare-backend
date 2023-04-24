package com.ktds.dsquare.board.like;

import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like,Long> {

    //사용자 입장에서 해당 게시글 좋아요 여부
    Like findByBoardTypeAndPostIdAndMember(BoardType boardType, Long postId, Member member);

    //게시글 입장에서 총 좋아요 수
    List<Like> findByBoardTypeAndPostId(BoardType boardType, Long postId);
}
