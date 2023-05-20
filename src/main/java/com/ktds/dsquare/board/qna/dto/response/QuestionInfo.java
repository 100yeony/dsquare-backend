package com.ktds.dsquare.board.qna.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.common.file.dto.AttachmentDto;
import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionInfo {

    private long qid;
    private CategoryResponse category;
    private BriefMemberInfo writerInfo;
    private String title;
    private String content;

    private List<String> tags;

    private AttachmentDto attachment;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime lastUpdateDate;

    // -------------------------
    // Additional information
    private long viewCnt;

    private long commentCnt;
    private long likeCnt;

    private boolean likeYn;
    // -------------------------


    public static QuestionInfo toDto(Question entity) {
        if (entity.getDeleteYn())
            return QuestionInfo.builder()
                    .qid(entity.getId())
                    .title("삭제된 게시글입니다")
                    .content("삭제된 게시글입니다.")
                    .build();
        else
            return toDto(entity, 0L, false);
    }

    public static QuestionInfo toDto(
            Question entity,
            long commentCnt,
            boolean likeYn
    ) {
        return QuestionInfo.builder()
                .qid(entity.getId())
                .category(CategoryResponse.toDto(entity.getCategory()))
                .writerInfo(BriefMemberInfo.toDto(entity.getWriter()))
                .title(entity.getTitle())
                .content(entity.getContent())
                .tags(entity.getTagList())
                .attachment(AttachmentDto.toDto(entity.getAttachment()))
                .createDate(entity.getCreateDate())
                .lastUpdateDate(entity.getLastUpdateDate())
                .viewCnt(entity.getViewCnt())
                .commentCnt(commentCnt)
                .likeCnt(entity.getLikeCnt())
                .likeYn(likeYn)
                .build();
    }

}
