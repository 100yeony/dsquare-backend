package com.ktds.dsquare.common.file;

import com.ktds.dsquare.common.file.dto.AttachmentDto;
import com.ktds.dsquare.common.file.dto.FileSavedDto;
import com.ktds.dsquare.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

    private final FileService fileService;

//    public AttachmentDto insertAttachment(String filePath, MultipartFile file) {
//        // TODO here
//        return null;
//    }

    @Transactional
    public AttachmentDto insertAttachment(Member member, MultipartFile file) throws IOException {
        try {
            FileSavedDto fileSavedDto = fileService.uploadFile(file);
            Attachment attachment = Attachment.toEntity(member, file, fileSavedDto);
            return AttachmentDto.toDto(attachmentRepository.save(attachment));
        } catch (IOException e) {
            log.error("Error while inserting attachment. {}", e.getMessage());
            throw e;
        }
    }

}
