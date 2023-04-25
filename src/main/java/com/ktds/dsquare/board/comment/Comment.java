package com.ktds.dsquare.board.comment;

import com.ktds.dsquare.board.comment.dto.CommentRegisterDto;
import com.ktds.dsquare.board.comment.dto.NestedCommentRegisterDto;
import com.ktds.dsquare.board.enums.BoardType;
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
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private Member writer;

    @Column(nullable = false, length = 500)
    private String content;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "originWriter")
    private Member originWriter;

    public static Comment toEntity(CommentRegisterDto request, Member writer, BoardType boardType, Long postId) {
        return Comment.builder()
                .writer(writer)
                .content(request.getContent())
                .boardType(boardType)
                .postId(postId)
                .createDate(LocalDateTime.now())
                .originWriter(null)
                .build();
    }

    public static Comment toNestedEntity(NestedCommentRegisterDto request, Member writer, BoardType boardType, Long postId, Member originWriter) {
        return Comment.builder()
                .writer(writer)
                .content(request.getContent())
                .boardType(boardType)
                .postId(postId)
                .createDate(LocalDateTime.now())
                .originWriter(originWriter)
                .build();
    }
}
