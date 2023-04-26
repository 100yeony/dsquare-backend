package com.ktds.dsquare.board.like;

import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.board.like.dto.LikeRequest;
import com.ktds.dsquare.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "LIKE_POST")
public class Like {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private BoardType boardType;

    @Column(nullable = false)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member")
    private Member member;

    @Column(nullable = false)
    private LocalDateTime createDate;

    public static Like toEntity(LikeRequest dto, BoardType boardType, Member user){
        LocalDateTime now = LocalDateTime.now();
        return Like.builder()
                .boardType(boardType)
                .postId(dto.getPostId())
                .member(user)
                .createDate(now)
                .build();
    }



}
