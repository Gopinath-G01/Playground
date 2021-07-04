package com.gopig.fumo.service;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gopig.fumo.exception.ScanServiceException;
import com.gopig.fumo.model.entities.ScanDetailsEntity;
import com.gopig.fumo.model.request.ScanInfo;
import com.gopig.fumo.model.validator.ValidationStatus;
import com.gopig.fumo.repository.ScanRepository;

/**
 * TODO:
 * https://stackoverflow.com/questions/45526151/how-to-save-multiple-tables-using-spring-boot-jparepository
 */
@Service
public class ScanService {

	@Autowired
	private ScanRepository scanRepository;

	@Value("${file.destination:db}")
	String destinationFromConfig;

	private String DESTINATION_DB = "db";
	private String FILE_NAME_PREFIX = "UPLOAD_";
	private String DEVICE_ID_PREFIX = "DEVICE_";

	public ScanDetailsEntity uploadFileWithMetaData(MultipartFile file, ScanInfo scanInfo, String destination) throws ScanServiceException {
		ScanDetailsEntity scanDetailsEntity = new ScanDetailsEntity();
		if (null != file && !file.isEmpty()) {
			destination = (null != destination)? destination:destinationFromConfig;
			if (destination.equalsIgnoreCase(DESTINATION_DB)) {
				try {
					String deviceId = (null != scanInfo.getDeviceId()) ? scanInfo.getDeviceId()
							: java.net.InetAddress.getLocalHost().getHostName();

					String userId = System.getProperty("user.name");

					scanDetailsEntity.setPatientId(scanInfo.getPatientId());
					scanDetailsEntity.setDeviceId(DEVICE_ID_PREFIX + deviceId);
					scanDetailsEntity.setScanRecommendedBy(scanInfo.getScanRecommendedBy());
					scanDetailsEntity.setScanSummary(scanInfo.getScanSummary());
					scanDetailsEntity.setFileType(file.getContentType());
					scanDetailsEntity.setFileName(FILE_NAME_PREFIX + file.getOriginalFilename());
					scanDetailsEntity.setFileSize(file.getSize());
					scanDetailsEntity.setRawContent(file.getBytes());
					scanDetailsEntity.setInsertedBy(userId);
					scanDetailsEntity.setLastUpdatedBy(userId);

				} catch (IOException exception) {
					exception.printStackTrace();
				}
				
				ScanDetailsEntity scanDetailsResponse = scanRepository.save(scanDetailsEntity);
				return scanDetailsResponse;
			} else {
				throw new ScanServiceException(101, "Unsupported Destination for file storage.");
			}
		} else {
			throw new ScanServiceException(102, "Input file is either empty or blank");
		}
	}

	public ScanDetailsEntity downloadFileById(String fileId) throws ScanServiceException, Exception {
		ScanDetailsEntity scanDetailsResponse = null;
		if (StringUtils.isBlank(fileId)) {
			scanDetailsResponse = scanRepository.getById(fileId);
		} else {
			throw new ScanServiceException(ValidationStatus.MISSING_GET_REQUEST_PARAMS.getValue(),
					ValidationStatus.MISSING_GET_REQUEST_PARAMS.getMessage());
		}

		return scanDetailsResponse;
	}

	/*
	 * public String getLoggedUser() throws Exception { String name =
	 * SecurityContextHolder.getContext().getAuthentication().getName(); return
	 * (!name.equals("anonymousUser")) ? name : null; }
	 */
}
