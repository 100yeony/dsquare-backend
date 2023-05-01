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
public class MyCommentInfo {

    private Long commentId;
    private MemberInfo writerInfo;
    private String content;
    private LocalDateTime createDate;
    private String originWriterName;
    private BoardType boardType;
    private Long postId;

    public static MyCommentInfo toDto(Comment comment) {
        String originWriterName;
        if(comment.getOriginWriter() != null){
            originWriterName = comment.getOriginWriter().getName();
        }else {
            originWriterName = null;
        }
        return MyCommentInfo.builder()
                .commentId(comment.getId())
                .writerInfo(MemberInfo.toDto(comment.getWriter()))
                .content(comment.getContent())
                .createDate(comment.getCreateDate())
                .originWriterName(originWriterName)
                .boardType(comment.getBoardType())
                .postId(comment.getPostId())
                .build();
    }
}
