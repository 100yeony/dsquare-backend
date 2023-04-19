package com.ktds.dsquare.common.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AwsProperties {

    /*** Property ***/
    private static String BUCKET_NAME;

    /*** Getter ***/
    public static String BUCKET_NAME() {
        return BUCKET_NAME;
    }

    /*** Setter ***/
    @Value("${aws.s3.bucket-name}")
    private void setBucketName(String bucketName) {
        BUCKET_NAME = bucketName;
        log.debug("BUCKET_NAME is set\n: {}", BUCKET_NAME);
    }

}
