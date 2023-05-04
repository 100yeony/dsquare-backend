package com.ktds.dsquare.board.tag;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
public class WeeklyTagDTO {

    @ApiModelProperty(notes="Talk ID", example = "1", required = true)
    private Long tagId;
    @ApiModelProperty(notes="count", example = "3", required = true)
    private Long tagCount;
    @ApiModelProperty(notes="작성일 정보", example = "2023-04-26 09:30:00", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createDate;

    public WeeklyTagDTO(Long tagId, Long tagCount, LocalDateTime createDate) {
        this.tagId = tagId;
        this.tagCount = tagCount;
        this.createDate = createDate;
    }

}
