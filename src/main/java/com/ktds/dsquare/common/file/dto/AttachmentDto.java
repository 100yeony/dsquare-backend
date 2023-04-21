package com.ktds.dsquare.common.file.dto;


import com.ktds.dsquare.common.file.Attachment;
import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class AttachmentDto {

    private Long id;
    private String filename;
    private String origFilename;
    private String path;
    private String url;
    private String extension;
    private Long size;
    private LocalDateTime createDate;

    private BriefMemberInfo owner;


    public static AttachmentDto toDto(Attachment entity) {
        return AttachmentDto.builder()
                .id(entity.getId())
                .filename(entity.getFilename())
                .origFilename(entity.getOriginalFilename())
                .path(entity.getPath())
                .url(entity.getUrl())
                .extension(entity.getExtension())
                .size(entity.getSize())
                .createDate(entity.getCreateDate())
                .owner(BriefMemberInfo.toDto(entity.getWriter()))
                .build();
    }

}
