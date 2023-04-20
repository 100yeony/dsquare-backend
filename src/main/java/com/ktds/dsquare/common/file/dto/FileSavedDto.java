package com.ktds.dsquare.common.file.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FileSavedDto {

    private String filename;
    private String extension;
    private String path;
    private String url;

}
