package com.gopig.fumo.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AWSS3Config {
 
    @Value("${aws.access_key_id}") private String accessKeyId;
    @Value("${aws.secret_access_key}") private String secretAccessKey;
    @Value("${aws.s3.region}") private String regionAsString;
    
    private Region defaultRegion = Region.AP_SOUTH_1;
 
    @Bean
    public S3Client getAmazonS3Cient() {
        final AwsBasicCredentials basicAWSCredentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        
        Region region = (StringUtils.isEmpty(regionAsString))? defaultRegion : Region.of(regionAsString); 
        // Get AmazonS3 client and return the s3Client object.
        return S3Client.builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(basicAWSCredentials))
//              .credentialsProvider(DefaultCredentialsProvider.create()) //alternate way of adding auth
                .build();
    }
}
