package com.ktds.dsquare.common.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
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
    public void uploadFile(MultipartFile file) throws IOException {
        log.info("File description | Original filename : {} | Name : {} | Size : {} | Content Type : {}",
                file.getOriginalFilename(),
                file.getName(),
                file.getSize(),
                file.getContentType());

        final String filename = removeExtension(file.getOriginalFilename());

        final String directory = determineDirectory();
        final String uniqueFilename = generateUniqueFilename(filename);
        final String filePath = directory + "/" + uniqueFilename;

        PutObjectResult result = upload(filePath, file);
    }
    private String removeExtension(String filename) {
        if (!StringUtils.hasText(filename))
            throw new RuntimeException("Filename is empty!");

        return filename.substring(0, filename.lastIndexOf("."));
    }
    private String determineDirectory() {
        return "/TestDir";
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
        return metadata;
    }

    private void download(String eTag) {
//        s3Client.get
    }

}
