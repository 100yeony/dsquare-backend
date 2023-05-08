package com.ktds.dsquare.common.file.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.ktds.dsquare.common.file.Attachment;
import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
import lombok.*;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachmentDto {
    // TODO divide into Req./Resp.

    private Long id;
    private String filename;
    private String origFilename;
    private String path;
    private String url;
    private String extension;
    private Long size;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createDate;

    private BriefMemberInfo owner;

    private boolean deleted;


    @Override
    public int hashCode() {
        return Objects.hash(id, filename, origFilename, path, url, extension, size);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        if (this == obj)
            return true;

        AttachmentDto o = (AttachmentDto)obj;
        return Objects.equals(this.id, o.id)
                && Objects.equals(this.filename, o.filename)
                && Objects.equals(this.origFilename, o.origFilename)
                && Objects.equals(this.path, o.path)
                && Objects.equals(this.url, o.url)
                && Objects.equals(this.extension, o.extension)
                && Objects.equals(this.size, o.size);
//                && Objects.equals(this.createDate, o.createDate);
    }

    public static AttachmentDto toDto(Attachment entity) {
        return ObjectUtils.isEmpty(entity)
                ? null
                : AttachmentDto.builder()
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
