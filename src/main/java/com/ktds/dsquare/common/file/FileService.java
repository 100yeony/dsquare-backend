package com.ktds.dsquare.common.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.ktds.dsquare.common.file.dto.FileSavedDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final AmazonS3 s3Client;


    @Transactional
    public FileSavedDto uploadFile(MultipartFile file) throws IOException {
        // TODO rename uploadFile() to upload()
        log.info("File description | Original filename : {} | Name : {} | Size : {} | Content Type : {}",
                file.getOriginalFilename(),
                file.getName(),
                file.getSize(),
                file.getContentType());

        final String filename = removeExtension(file.getOriginalFilename());

        final String directory = determineDirectory();
        final String uniqueFilename = generateUniqueFilename(filename);
        final String filePath = directory + uniqueFilename;

        PutObjectResult result = upload(filePath, file); // TODO here
        return FileSavedDto.builder()
                .filename(uniqueFilename)
                .extension(extractExtension(file.getOriginalFilename()))
                .path(filePath)
                .url(makeAccessibleUrl(filePath))
                .build();
    }
    private String removeExtension(String filename) {
        if (!StringUtils.hasText(filename))
            throw new RuntimeException("Filename is empty!");

        return filename.substring(0, filename.lastIndexOf("."));
    }
    public String extractExtension(String filename) {
        if (!StringUtils.hasText(filename))
            throw new RuntimeException("Filename is empty!");

        return filename.substring(filename.lastIndexOf(".") + 1);
    }
    private String determineDirectory() {
        // TODO determine directory depending on requesting user or something else
        return "TestDir/";
    }
    private String generateUniqueFilename(String filename) {
        return filename + "_" + UUID.randomUUID();
    }
    private PutObjectResult upload(String key, MultipartFile file) throws IOException {
        log.info("[File --> S3] Upload object as {}", key);

        ObjectMetadata metadata = getMetadata(file);
        try {
            PutObjectResult result = s3Client.putObject(AwsProperties.BUCKET_NAME(), key, file.getInputStream(), metadata);
            log.info("!! File uploaded on S3 !!");
            log.info("Metadata | Content Type : {}", result.getMetadata().getContentType());
            log.info("Metadata | Archive Status : {}", result.getMetadata().getArchiveStatus());
            log.info("Metadata | ETag(Entity-Tag) : {}", result.getMetadata().getETag());
            log.info("Metadata | Expiration Time : {}", result.getMetadata().getExpirationTime());
            log.info("Metadata | Last Modified : {}", result.getMetadata().getLastModified());
            log.info("Metadata | Version ID : {}", result.getMetadata().getVersionId());
            log.info("Metadata | Raw Metadata\n: {}", result.getMetadata().getRawMetadata());
            return result;
        } catch (IOException e) {
            log.error("Error while putting. {}", e.getMessage());
            throw e;
        }
    }
    private ObjectMetadata getMetadata(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        // TODO custom header?로 확장자 추가?
        return metadata;
    }
    private String makeAccessibleUrl(String filePath) {
        return "https://" + AwsProperties.BUCKET_NAME() + ".s3.amazonaws.com/" + filePath;
    }

    private void download(String eTag) {
//        s3Client.get
    }

    public void delete(String key) {
        // TODO separate bucket
        log.info("Trying to delete {}", key);
//        s3Client.deleteObject(AwsProperties.BUCKET_NAME(), key);
    }

}
