package com.ktds.dsquare.board.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class NestedTalkCommentInfo {

    private Long commentId;
    private MemberInfo writerInfo;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createDate;
    private String originWriterNickname;

    public static NestedTalkCommentInfo toDto(Comment comment) {
        return NestedTalkCommentInfo.builder()
                .commentId(comment.getId())
                .writerInfo(MemberInfo.toDto(comment.getWriter()))
                .content(comment.getContent())
                .createDate(comment.getCreateDate())
                .originWriterNickname(comment.getOriginWriter().getNickname())
                .build();
    }
}
