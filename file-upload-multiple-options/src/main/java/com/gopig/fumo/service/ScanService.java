package com.gopig.fumo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gopig.fumo.exception.ScanServiceException;
import com.gopig.fumo.model.entities.FileStorageDetailsEntity;
import com.gopig.fumo.model.entities.ScanDetailsEntity;
import com.gopig.fumo.model.request.ScanInfo;
import com.gopig.fumo.model.response.UploadResponse;
import com.gopig.fumo.model.validator.ValidationResult;
import com.gopig.fumo.model.validator.ValidationStatus;
import com.gopig.fumo.repository.FileStorageDetailsRepository;
import com.gopig.fumo.repository.ScanRepository;
import com.gopig.fumo.validator.ValidationHelper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static com.gopig.fumo.utils.FileUploadConstants.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * service class implementation to persist files to given destination
 *
 * @author Gopinath_G01
 */
@Service
public class ScanService {

    private static final Logger log = LoggerFactory.getLogger(ScanService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ValidationHelper validationHelper;

    @Autowired
    private ScanRepository scanRepository;

    @Autowired
    private FileStorageDetailsRepository storageDetailsRepository;

    @Value("${file.destination:db}")
    String destinationFromConfig;

    @Autowired
    AWSS3Service awsService;

    public UploadResponse uploadFileWithMetaData(MultipartFile[] files, String scanInfoJson, String destination)
            throws ScanServiceException {
        UploadResponse uploadResponse = null;
        ScanInfo scanInfo = null;

        try {
            scanInfo = objectMapper.readValue(scanInfoJson, ScanInfo.class);
        } catch (JsonProcessingException exception) {
            log.error("Cannot parse ScanInfo.", exception);
            throw new ScanServiceException("101", "Unable to parse the contents of scanInfo.");
        }

        if (null == scanInfo) {
            throw new ScanServiceException("101", "Unable to parse the contents of scanInfo.");
        }

        // Validating both the file as well as the scanInfo
        List<ValidationResult> validateResults = validationHelper.validateUpload(files, scanInfo);
        List<ValidationResult> validationFailures = validateResults.stream()
                .filter(result ->
                        !ValidationStatus.SUCCESS.getValue().equals(result.getCode())).collect(Collectors.toList()
                );

        if (!CollectionUtils.isEmpty(validationFailures)) {
            ScanServiceException scanServiceException = new ScanServiceException("103", "Input Validation Failed");

            validationFailures.stream().forEach(failureResult -> scanServiceException
                    .addErrorDetail(failureResult.getCode(), failureResult.getMessage()));

            throw scanServiceException;
        }

        destination = (null != destination) ? destination : destinationFromConfig;

        if (DESTINATION_DB.equalsIgnoreCase(destination)) {
            uploadResponse = uploadToDB(files, scanInfo, null);
        } else if (DESTINATION_LOCAL_FILESYSTEM.equalsIgnoreCase(destination)) {
            uploadResponse = uploadToLocalFS(files, scanInfo);
        } else if (DESTINATION_CLOUD.equalsIgnoreCase(destination)) {
            uploadResponse = uploadToCloud(files, scanInfo);
        } else {
            throw new ScanServiceException("104", "Unsupported Destination for file storage.");
        }
        uploadResponse.setDownloadUri(constructDownloadURL(uploadResponse.getScanId()));
        return uploadResponse;
    }

    /**
     * Method to construct download URL based on the scanId
     *
     * @param scanId
     * @return downloadURL
     */
    private String constructDownloadURL(String scanId) {
        String fileDownloadUri = null;
        if (StringUtils.isNotEmpty(scanId)) {
            fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/")
                    .path(scanId)
                    .toUriString();
        }
        return fileDownloadUri;
    }

    /**
     * Method to upload the file to Cloud, by default into AWS S3 bucket, and store
     * incoming scanInfo as <scanId>_scanInfo.json
     *
     * @param file
     * @param scanInfo
     * @return uploadResponse
     */
    private UploadResponse uploadToCloud(MultipartFile[] files, ScanInfo scanInfo) {
        Map<String, String> cloudData = awsService.uploadToCloud(files, scanInfo);
        return uploadToDB(files, scanInfo, cloudData);
    }

    /**
     * Method to Upload/Store the file into a local FileSystem and store incoming
     * scanInfo as <scanId>_scanInfo.json
     *
     * @param files
     * @param scanInfo
     * @return uploadResponse
     */
    private UploadResponse uploadToLocalFS(MultipartFile[] files, ScanInfo scanInfo) {
        UploadResponse uploadResponse = new UploadResponse();
        log.debug("{}, {}", files, scanInfo);
        return uploadResponse;
    }

    /**
     * Method to store the file along with details from scanInfo into a database
     *
     * @param file
     * @param scanInfo
     * @return uploadResponse
     */
    public UploadResponse uploadToDB(MultipartFile[] files, ScanInfo scanInfo, Map<String, String> cloudData) throws ScanServiceException {
        ScanDetailsEntity scanDetailsEntity = new ScanDetailsEntity();
        UploadResponse uploadResponse = null;

        try {
            String deviceId = (null != scanInfo.getDeviceId()) ? scanInfo.getDeviceId()
                    : java.net.InetAddress.getLocalHost().getHostName();

            String userId = System.getProperty("user.name");

            scanDetailsEntity.setPatientId(scanInfo.getPatientId());
            scanDetailsEntity.setDeviceId(DEVICE_ID_PREFIX + deviceId);
            scanDetailsEntity.setScanRecommendedBy(scanInfo.getScanRecommendedBy());
            scanDetailsEntity.setScanSummary(scanInfo.getScanSummary());
            scanDetailsEntity.setInsertedBy(userId);
            scanDetailsEntity.setLastUpdatedBy(userId);
            ScanDetailsEntity scanDetailsResponse = scanRepository.save(scanDetailsEntity);

            List<FileStorageDetailsEntity> storageDetailsEntitiesCollection = new ArrayList<>();
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                String fileName = FILE_NAME_PREFIX +
                        org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
                FileStorageDetailsEntity storageDetailsEntity = new FileStorageDetailsEntity();
                storageDetailsEntity.setFileType(file.getContentType());
                storageDetailsEntity.setFileName(fileName);
                storageDetailsEntity.setFileSize(file.getSize());
                if (null != cloudData && cloudData.size() > 0) {
                    storageDetailsEntity.setFileCloudURL(cloudData.get(fileName));
                    storageDetailsEntity.setRawContent(file.getBytes());
                    if (StringUtils.isNotEmpty(cloudData.get(fileName)))
                        storageDetailsEntity.setSyncedWithCloud(true);
                }
                storageDetailsEntity.setScanDetails(scanDetailsResponse);
                storageDetailsEntitiesCollection.add(storageDetailsEntity);
            }

            List<FileStorageDetailsEntity> storageDetailsResponseEntity = storageDetailsRepository
                    .saveAll(storageDetailsEntitiesCollection);
            uploadResponse = constructUploadResponse(storageDetailsResponseEntity);

        } catch (UnknownHostException unknownHostException) {
            log.error("Localhost name could notbe resolved into an address.");
            throw new ScanServiceException("105", "Localhost name could notbe resolved into an address.");
        } catch (IOException ioException) {
            log.error("Could not access the contents of File.");
            throw new ScanServiceException("106", "Could not access the contents of File.");
        } catch (Exception exception) {
            log.error("Exception occured while trying to store the file to DB.");
            throw new ScanServiceException("107",
                    "Exception while storing file: " + exception.getMessage() + " | " + exception.getCause());
        }
        return uploadResponse;
    }

    /**
     * Method to construct the UploadResponse based on the ScanDetailsEntity
     * response object post the CRUD operation
     *
     * @param scanDetailsEntity
     * @return uploadResponse
     */
    public UploadResponse constructUploadResponse(List<FileStorageDetailsEntity> storageDetailsResponseEntity) {

        UploadResponse uploadResponse = new UploadResponse();

        if (null != storageDetailsResponseEntity && !storageDetailsResponseEntity.isEmpty()) {
            for (FileStorageDetailsEntity fileStorageDetailsEntity : storageDetailsResponseEntity) {
                ScanDetailsEntity scanDetailsEntity = fileStorageDetailsEntity.getScanDetails();
                uploadResponse.setScanId(scanDetailsEntity.getScanId());

                uploadResponse.addFileDetails(fileStorageDetailsEntity.getFileId(),
                        fileStorageDetailsEntity.getFileName(), fileStorageDetailsEntity.getFileType(),
                        fileStorageDetailsEntity.getFileSize(), fileStorageDetailsEntity.getFileCloudURL());
            }

            uploadResponse.setResult(UPLOAD_RESULT_SUCCESS);
        } else {
            uploadResponse.setResult(UPLOAD_RESULT_FAILED);
        }

        return uploadResponse;
    }

    /**
     * @param fileId
     * @return ScanDetailsEntity
     * @throws ScanServiceException
     * @throws Exception
     */
    public FileStorageDetailsEntity downloadFileById(String fileId) throws ScanServiceException {
        FileStorageDetailsEntity fileStorageDetailsEntity = null;
        if (StringUtils.isBlank(fileId)) {
            fileStorageDetailsEntity = storageDetailsRepository.getById(fileId);
        } else {
            throw new ScanServiceException(ValidationStatus.MISSING_GET_REQUEST_PARAMS.getValue(),
                    ValidationStatus.MISSING_GET_REQUEST_PARAMS.getMessage());
        }

        return fileStorageDetailsEntity;
    }

    /*
     * public String getLoggedUser() throws Exception { String name =
     * SecurityContextHolder.getContext().getAuthentication().getName(); return
     * (!name.equals("anonymousUser")) ? name : null; }
     */
}
