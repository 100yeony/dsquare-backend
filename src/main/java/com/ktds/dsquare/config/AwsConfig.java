package com.ktds.dsquare.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    @Bean
    public AWSCredentials awsCredentials(
            @Value("${aws.key.access}") String accessKey,
            @Value("${aws.key.secret}") String secretKey
    ) {
        return new BasicAWSCredentials(accessKey, secretKey);
    }

    @Bean
    public AmazonS3 amazonS3(AWSCredentials credentials) {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();
    }

}
