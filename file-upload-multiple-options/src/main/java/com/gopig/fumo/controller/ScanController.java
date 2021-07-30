package com.gopig.fumo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.gopig.fumo.exception.ScanServiceException;
import com.gopig.fumo.model.entities.FileStorageDetailsEntity;
import com.gopig.fumo.model.response.UploadResponse;
import com.gopig.fumo.service.ScanService;

@RestController
@RequestMapping("/app/v1")
public class ScanController {

    private static final Logger log = LoggerFactory.getLogger(ScanController.class);

    @Autowired
    private ScanService scanService;

    @PostMapping(value = {"/upload", "/upload/{destination}"})
    public UploadResponse multiUpload(@RequestParam("files") MultipartFile[] files,
                                      @RequestParam("scanInfo") String scanInfoJson, 
                                      @PathVariable(required = false) String destination)
            throws ScanServiceException {
        log.info("Request received to upload file");
        log.info("Number of Files received: {}", ((null != files) ? files.length : 0));
        log.info("Upload Metadata details : {}", scanInfoJson);

        UploadResponse uploadResponse = null;
        // service call to upload the file
        uploadResponse = scanService.uploadFileWithMetaData(files, scanInfoJson, destination);

        return uploadResponse;
    }

    @GetMapping("/download/files/{id}")
    public ResponseEntity<Object> download(@PathVariable String id) throws ScanServiceException {

        FileStorageDetailsEntity responseEntity = null;
        UploadResponse uploadResponse = null;
        responseEntity = scanService.downloadFileById(id);

        if (null != responseEntity) {
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(responseEntity.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + responseEntity.getFileName())
                    .body(new ByteArrayResource(responseEntity.getRawContent()));
        } else {
            return ResponseEntity.internalServerError().body(uploadResponse);
        }
    }

//	@GetMapping(value = "/download/", produces="application/zip")
//	public void zipDownload(@RequestParam List<String> name, HttpServletResponse response) throws IOException {
//		ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
//		for (String fileName : name) {
//			FileSystemResource resource = new FileSystemResource(fileBasePath + fileName);
//			ZipEntry zipEntry = new ZipEntry(resource.getFilename());
//			zipEntry.setSize(resource.contentLength());
//			zipOut.putNextEntry(zipEntry);
//			StreamUtils.copy(resource.getInputStream(), zipOut);
//			zipOut.closeEntry();
//		}
//		zipOut.finish();
//		zipOut.close();
//		response.setStatus(HttpServletResponse.SC_OK);
//		response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + zipFileName + "\"");
//	}
}
