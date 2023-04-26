package com.ktds.dsquare.board.like;

import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like,Long> {

    //사용자 입장에서 해당 게시글 좋아요 여부
    Boolean existsByBoardTypeAndPostIdAndMember(BoardType boardType, Long postId, Member member);

    //게시글 입장에서 총 좋아요 수
    Long countByBoardTypeAndPostId(BoardType boardType, Long postId);

    //좋아요 삭제
    Like findByBoardTypeAndPostIdAndMember(BoardType boardType, Long postId, Member member);

}
