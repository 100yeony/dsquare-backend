package com.ktds.dsquare.common.file;

import com.ktds.dsquare.common.annotatin.AuthUser;
import com.ktds.dsquare.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping("/attachment-test") // TODO
    public ResponseEntity<?> test(@AuthUser Member member, MultipartFile file) throws IOException {
        return new ResponseEntity<>(attachmentService.insertAttachment(member, file), HttpStatus.OK);
    }

}
