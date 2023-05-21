package com.ktds.dsquare.board.qna.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.common.file.Attachment;
import com.ktds.dsquare.common.file.dto.AttachmentDto;
import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class QuestionRegisterResponse {

    private long qid;

    private CategoryResponse category;
    private BriefMemberInfo writerInfo;
    private String title;
    private String content;

    private AttachmentDto attachment;
    private List<String> tags;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createDate;

    // TODO regarded as useless
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime lastUpdateDate;

    private long viewCnt;
    private long likeCnt;
    private boolean likeYn;
    private long commentCnt;
    // -------------------------


    public static QuestionRegisterResponse toDto(Question entity, Attachment attachment) {
        return QuestionRegisterResponse.builder()
                .qid(entity.getId())
                .category(CategoryResponse.toDto(entity.getCategory()))
                .writerInfo(BriefMemberInfo.toDto(entity.getWriter()))
                .title(entity.getTitle())
                .content(entity.getContent())
                .attachment(AttachmentDto.toDto(entity.getAttachment()))
                .tags(
                        ObjectUtils.isEmpty(entity.getTags())
                        ? null
                        : entity.getTags().stream()
                                .map(postTag -> postTag.getTag().getName())
                                .collect(Collectors.toList())
                )
                .attachment(AttachmentDto.toDto(attachment))
                .createDate(entity.getCreateDate())

                .lastUpdateDate(LocalDateTime.MAX)
                .viewCnt(Long.MAX_VALUE)
                .likeCnt(Long.MAX_VALUE)
                .likeYn(false)
                .commentCnt(Long.MAX_VALUE)
                .build();
    }

}
