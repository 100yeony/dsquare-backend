package com.ktds.dsquare.board.comment.dto;

import com.ktds.dsquare.board.comment.Comment;
import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentRegisterResponse {

    private long id;
    private long postId;
    private BriefMemberInfo writerInfo;


    public static CommentRegisterResponse toDto(Comment entity) {
        return CommentRegisterResponse.builder()
                .id(entity.getId())
                .postId(entity.getPostId())
                .writerInfo(BriefMemberInfo.toDto(entity.getWriter()))
                .build();
    }

}
