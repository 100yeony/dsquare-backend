package com.ktds.dsquare.common.file;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;


    @PostMapping("/file/upload")
    public ResponseEntity<String> uploadFile(MultipartFile file) throws Exception {
        return new ResponseEntity<>(fileService.uploadFile(file), HttpStatus.CREATED);
    }

}
