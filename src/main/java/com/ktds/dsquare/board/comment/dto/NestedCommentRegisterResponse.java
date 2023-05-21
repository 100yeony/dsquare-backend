package com.ktds.dsquare.board.comment.dto;

import com.ktds.dsquare.board.comment.Comment;
import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NestedCommentRegisterResponse {

    private long id;
    private BriefMemberInfo writer;
    private String content;
    private LocalDateTime createDate;

    private long postId;
    private BriefMemberInfo originWriter;


    public static NestedCommentRegisterResponse toDto(Comment entity) {
        return NestedCommentRegisterResponse.builder()
                .id(entity.getId())
                .writer(BriefMemberInfo.toDto(entity.getWriter()))
                .content(entity.getContent())
                .createDate(entity.getCreateDate())
                .postId(entity.getPostId())
                .originWriter(BriefMemberInfo.toDto(entity.getOriginWriter()))
                .build();
    }

}
