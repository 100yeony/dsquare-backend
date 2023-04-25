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
public class NestedCommentInfo {

    private Long commentId;
    private MemberInfo writerInfo;
    private String content;
    private LocalDateTime createDate;
    private String originWriterName;

    public static NestedCommentInfo toDto(Comment comment) {
        return NestedCommentInfo.builder()
                .commentId(comment.getId())
                .writerInfo(MemberInfo.toDto(comment.getWriter()))
                .content(comment.getContent())
                .createDate(comment.getCreateDate())
                .originWriterName(comment.getOriginWriter().getName())
                .build();
    }
}