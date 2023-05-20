package com.ktds.dsquare.board.qna.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
import lombok.*;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BriefQuestionInfo {

    private long qid;
    private CategoryResponse category;
    private BriefMemberInfo writerInfo;

    private String title;
    private String content;

    private List<String> tags;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createDate;

    // -------------------------
    // Additional information
    private long viewCnt;

    private long commentCnt;
    private long answerCnt;
    private long likeCnt;

    private boolean atcYn;
    private boolean managerAnswerYn;
    private boolean likeYn;
    // -------------------------


    public static BriefQuestionInfo toDto(
            Question entity,
            long commentCnt,
            long answerCnt,
            boolean isManagerAnswered,
            boolean isLiked
    ) {
        return BriefQuestionInfo.builder()
                .qid(entity.getId())
                .category(CategoryResponse.toDto(entity.getCategory()))
                .writerInfo(BriefMemberInfo.toDto(entity.getWriter()))
                .title(entity.getTitle())
                .content(entity.getContent())
                .tags(entity.getTagList())
                .createDate(entity.getCreateDate())
                .viewCnt(entity.getViewCnt())
                .commentCnt(commentCnt)
                .answerCnt(answerCnt)
                .likeCnt(entity.getLikeCnt())
                .atcYn(!ObjectUtils.isEmpty(entity.getAttachment()))
                .managerAnswerYn(isManagerAnswered)
                .likeYn(isLiked)
                .build();
    }

}
