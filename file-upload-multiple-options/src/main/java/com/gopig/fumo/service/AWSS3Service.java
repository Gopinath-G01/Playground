package com.gopig.fumo.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gopig.fumo.exception.ScanServiceException;
import com.gopig.fumo.model.request.ScanInfo;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import static com.gopig.fumo.utils.FileUploadConstants.FILE_NAME_PREFIX;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AWSS3Service {

    private static final Logger LOG = LoggerFactory.getLogger(AWSS3Service.class);

    @Autowired
    private S3Client s3Client;
    @Value("${aws.s3.bucket}")
    private String bucketName;
    @Value("${aws.s3.region}")
    private String regionAsString;

    private Region defaultRegion = Region.AP_SOUTH_1;

    private String putS3Object(String fileName, byte[] fileContent, Map<String, String> metadata) {

        try {
            PutObjectRequest putObj = PutObjectRequest.builder().bucket(bucketName).key(fileName).metadata(metadata)
                    .build();

            PutObjectResponse response = s3Client.putObject(putObj, RequestBody.fromBytes(fileContent));

            return response.eTag();
        } catch (S3Exception s3Exception) {
            LOG.error("Error while uploading data to cloud.", s3Exception);
        }
        return StringUtils.EMPTY;
    }

    public Map<String, String> uploadToCloud(MultipartFile[] files, ScanInfo scanInfo) throws ScanServiceException {
        Map<String, String> cloudData = null;
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            Map<String, String> metadata = new HashMap<>();
            try {
                cloudData = new HashMap<>();
                String fileName = FILE_NAME_PREFIX
                        + org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
                byte[] fileContent = file.getBytes();
                metadata.put("fileType", file.getContentType());
                metadata.put("deviceId", scanInfo.getDeviceId());
                metadata.put("patientId", scanInfo.getPatientId());
                metadata.put("scanRecommendedBy", scanInfo.getScanRecommendedBy());

                String result = putS3Object(fileName, fileContent, metadata);

                if (StringUtils.isNotEmpty(result)) {
                    Region region = (StringUtils.isEmpty(regionAsString)) ? defaultRegion : Region.of(regionAsString);
                    LOG.info("Region as String: {}", region);
                    String cloudURL = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;
                    cloudData.put(fileName, cloudURL);
                }

            } catch (IOException ioException) {
                LOG.error("Could not access the contents of File.");
                throw new ScanServiceException("106", "Could not access the contents of File.", ioException.getCause());
            }
        }

        return cloudData;
    }
}
