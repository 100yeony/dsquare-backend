package com.ktds.dsquare.board.comment.dto;

import com.ktds.dsquare.board.comment.Comment;
import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.member.dto.response.MemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private Long commentId;
    private MemberInfo writerInfo;
    private String content;
    private BoardType boardType;
    private Long postId;
    private LocalDateTime createDate;
    private MemberInfo originWriter;

    public static CommentResponse toDto(Comment comment) {
        MemberInfo originWriterInfo =
                comment.getOriginWriter() != null ?
                MemberInfo.toDto(comment.getOriginWriter()) : null;
        return CommentResponse.builder()
                .commentId(comment.getId())
                .writerInfo(MemberInfo.toDto(comment.getWriter()))
                .content(comment.getContent())
                .boardType(comment.getBoardType())
                .postId(comment.getPostId())
                .createDate(comment.getCreateDate())
                .originWriter(originWriterInfo)
                .build();
    }
}
