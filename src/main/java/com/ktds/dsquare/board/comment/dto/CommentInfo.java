package com.ktds.dsquare.board.comment.dto;

import com.ktds.dsquare.board.comment.Comment;
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
public class CommentInfo {

    private Long commentId;
    private MemberInfo writerInfo;
    private String content;
    private LocalDateTime createDate;

    public static CommentInfo toDto(Comment comment) {
        return CommentInfo.builder()
                .commentId(comment.getId())
                .writerInfo(MemberInfo.toDto(comment.getWriter()))
                .content(comment.getContent())
                .createDate(comment.getCreateDate())
                .build();
    }
}
