package com.ktds.dsquare.board.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ktds.dsquare.board.comment.Comment;
import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
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
    private BriefMemberInfo writerInfo;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+9")
    private LocalDateTime createDate;
    private String originWriterName;

    public static NestedCommentInfo toDto(Comment comment) {
        return NestedCommentInfo.builder()
                .commentId(comment.getId())
                .writerInfo(BriefMemberInfo.toDto(comment.getWriter()))
                .content(comment.getContent())
                .createDate(comment.getCreateDate())
                .originWriterName(comment.getOriginWriter().getName())
                .build();
    }
}
