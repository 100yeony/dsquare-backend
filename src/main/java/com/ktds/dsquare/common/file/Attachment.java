package com.ktds.dsquare.common.file;

import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.common.file.dto.FileSavedDto;
import com.ktds.dsquare.member.Member;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Attachment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_file_name", unique = true)
    private String filename;
    @Column(name = "origin_file_name", nullable = false)
    private String originalFilename;

    @Column(name = "store_path", nullable = false)
    private String path;
    @Column(name = "file_url", nullable = false)
    private String url;

    @Column(nullable = false, length = 5)
    private String extension;

    @Column(name = "file_size")
    private Long size;

    @Column(nullable = false)
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member writer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Question post; // TODO Grand-refactoring


    public void linkPost(Question post) {
        this.post = post;
    }
    public void delinkPost() {
        this.post = null;
    }


    public static Attachment toEntity(Member owner, MultipartFile file, FileSavedDto savedInfo) {
        return Attachment.builder()
                .filename(savedInfo.getFilename())
                .originalFilename(file.getOriginalFilename())
                .path(savedInfo.getPath())
                .url(savedInfo.getUrl())
                .extension(savedInfo.getExtension())
                .size(file.getSize())
                .createDate(LocalDateTime.now())
                .writer(owner)
                .build();
    }
}
