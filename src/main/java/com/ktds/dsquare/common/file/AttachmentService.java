package com.ktds.dsquare.common.file;

import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.common.file.dto.AttachmentDto;
import com.ktds.dsquare.common.file.dto.FileSavedDto;
import com.ktds.dsquare.member.Member;
import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @Transactional // TODO check usability
    public AttachmentDto insertAttachment(Member member, MultipartFile file) throws RuntimeException {
        if (ObjectUtils.isEmpty(file))
            throw new RuntimeException("Given file is empty.");

        try {
            FileSavedDto fileSavedDto = fileService.uploadFile(file);
            Attachment attachment = Attachment.toEntity(member, file, fileSavedDto);
            return AttachmentDto.toDto(attachmentRepository.save(attachment));
        } catch (IOException e) {
            log.error("Error while inserting attachment. {}", e.getMessage());
            throw new RuntimeException("Could not insert attachment");
        }
    }

    @Transactional
    public Attachment saveAttachment(Member member, @NotNull MultipartFile file) throws RuntimeException {
        if (ObjectUtils.isEmpty(file))
            return null;
//            throw new RuntimeException("Given file is empty.");

        try {
            FileSavedDto fileSavedDto = fileService.uploadFile(file);
            Attachment attachment = Attachment.toEntity(member, file, fileSavedDto);
            return attachmentRepository.save(attachment);
        } catch (IOException e) {
            log.error("Error while inserting attachment. {}", e.getMessage());
            throw new RuntimeException("Could not save attachment.");
        }
    }
    @Transactional
    public Attachment saveAttachment(Member member, @NotNull MultipartFile file, Question post) throws RuntimeException {
        if (ObjectUtils.isEmpty(file))
            return null;

        Attachment savedAttachment = saveAttachment(member, file);
        savedAttachment.linkPost(post);
        return savedAttachment;
    }

    @Transactional
    public void updateAttachment(AttachmentDto updatedAttachment, Attachment existingAttachment) {
        // TODO AttachmentDto --> AttachmentUpdateDto

        List<Attachment> deletedAttachment = filterDeletedAttachment(updatedAttachment, existingAttachment);
        deleteAttachment(deletedAttachment);
    }
    private List<Attachment> filterDeletedAttachment(AttachmentDto updatedAttachment, Attachment existingAttachment) {
        return filterDeletedAttachment(List.of(updatedAttachment), List.of(existingAttachment));
    }
    private List<Attachment> filterDeletedAttachment(List<AttachmentDto> updatedAttachment, List<Attachment> existingAttachment) {
        determineFalsification(updatedAttachment, existingAttachment);

        List<Attachment> deletedAttachment = new ArrayList<>();

        int len = updatedAttachment.size();
        for (int i = 0; i < len; ++i) {
            if (updatedAttachment.get(i).isDeleted())
                deletedAttachment.add(existingAttachment.get(i));
        }
        return deletedAttachment;
    }
    public void determineFalsification(List<AttachmentDto> updatedAttachment, List<Attachment> existingAttachment) {
        if (ObjectUtils.isEmpty(existingAttachment))
            return;
        if (ObjectUtils.isEmpty(updatedAttachment)
            || updatedAttachment.size() != existingAttachment.size())
            throw new IllegalArgumentException("Bad Request. Attachment size mismatched.");

        // Preprocessing
//        updatedAttachment.sort(Comparator.comparingLong(AttachmentDto::getId));
//        existingAttachment.sort(Comparator.comparingLong(Attachment::getId));
        // TODO occurs un-trackable exception

        int len = updatedAttachment.size();
        for (int i = 0; i < len; ++i) {
            AttachmentDto target = updatedAttachment.get(i);
            AttachmentDto original = AttachmentDto.toDto(existingAttachment.get(i));
            if (isFalsified(target, original))
                throw new IllegalArgumentException("Bad Request. Requested attachment is falsified!!!");
        }
    }
    public boolean isFalsified(AttachmentDto target, AttachmentDto original) {
        if (ObjectUtils.isEmpty(original))
            throw new IllegalArgumentException("The original cannot be null!");
        if (ObjectUtils.isEmpty(target))
            return false;
        return !original.equals(target);
    }

    @Transactional
    public void deleteAttachmentByPostDeletion(List<Attachment> deleted) {
        // TODO 게시글 영속성 확인?

        deleteAttachment(deleted);
    }
    private void deleteAttachment(List<Attachment> deleted) {
        if (ObjectUtils.isEmpty(deleted))
            return;

        deleted.parallelStream()
                .forEach(attachment -> {
                    fileService.delete(attachment.getPath());
                    attachment.delinkPost();
                });
    }

}
