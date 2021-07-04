package com.gopig.fumo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gopig.fumo.exception.ScanServiceException;
import com.gopig.fumo.model.entities.ScanDetailsEntity;
import com.gopig.fumo.model.request.ScanInfo;
import com.gopig.fumo.model.response.UploadResponse;
import com.gopig.fumo.model.validator.ValidationResult;
import com.gopig.fumo.model.validator.ValidationStatus;
import com.gopig.fumo.service.ScanService;
import com.gopig.fumo.validator.ValidationHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app/v1")
public class ScanController {

	private static final Logger log = LoggerFactory.getLogger(ScanController.class);

	@Autowired
	private ScanService scanService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ValidationHelper validationHelper;

	@PostMapping(value = {"/upload", "/upload/{destination}"})
	public UploadResponse upload(@RequestParam("file") MultipartFile file,
			@RequestParam("scanInfo") String scanInfoJson,
			@PathVariable(required = false) String destination) throws ScanServiceException {
		log.info("Request received to upload file");
		UploadResponse uploadResponse;
		ScanInfo scanInfo = null;

		if (null != scanInfoJson) {
			try {
				scanInfo = objectMapper.readValue(scanInfoJson, ScanInfo.class);
				log.info("ScanId received: {}", scanInfo.getScanId());
			} catch (JsonProcessingException exception) {
				log.error("Cannot parse ScanInfo.", exception);
			}
		}
		List<ValidationResult> validateResults = validationHelper.validateUpload(scanInfo, file);
		List<ValidationResult> validationFailures = validateResults.stream()
				.filter(result -> result.getCode() != ValidationStatus.SUCCESS.getValue()).collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(validationFailures)) {
			uploadResponse = new UploadResponse();
			validationFailures.stream().forEach(failureResult -> uploadResponse.addErrorDetails(failureResult.getCode(),
					failureResult.getMessage()));
		} else {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			String scanId = null;
			String fileDownloadUri = null;
			ScanDetailsEntity responseEntity = scanService.uploadFileWithMetaData(file, scanInfo,destination);
			if (null != responseEntity) {
				scanId = responseEntity.getScanId();
				log.info("upload ScanId: " + responseEntity.getScanId());
				fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/app/v1/download/").path(scanId)
						.toUriString();
			}
			uploadResponse = new UploadResponse(scanId, fileName, fileDownloadUri, file.getContentType(),
					file.getSize());
		}
		return uploadResponse;
	}

	@PostMapping(value = {"/multiUpload","/multiUpload/{destination}"})
	public List<UploadResponse> multiUpload(@RequestParam("files") MultipartFile[] files,
			@RequestParam("scanInfo") String scanInfoJson,
			@PathVariable(required = false) String destination) {
		
		/* 
		 * TODO: Normalize the table structure by splitting 
		 * individual file details from common upload metaData
		 */
		
		return Arrays.asList(files).stream().map(file -> {
			UploadResponse uploadResponse = null;
			try {
				uploadResponse = upload(file, scanInfoJson,destination);
			} catch (ScanServiceException e) {
				e.printStackTrace();
			}
			return uploadResponse;
		}
		
		).collect(Collectors.toList());
	}

	@GetMapping("/download/{id}")
	public ResponseEntity<Resource> download(@PathVariable String id) throws ScanServiceException, Exception {
		ScanDetailsEntity responseEntity = scanService.downloadFileById(id);

		if (null != responseEntity) {
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(responseEntity.getFileType()))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + responseEntity.getFileName())
					.body(new ByteArrayResource(responseEntity.getRawContent()));
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
