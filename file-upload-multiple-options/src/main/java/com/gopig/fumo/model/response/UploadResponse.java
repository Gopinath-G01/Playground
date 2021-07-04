package com.gopig.fumo.model.response;

import java.util.ArrayList;
import java.util.List;

public class UploadResponse {
    private String fileName;
    private String downloadUri;
    private String fileType;
    private String scanId;
    private long size;
    private List<ErrorDetails> errorDetails;

    private class ErrorDetails {
        private int errorCode;
        private String errorMessage;

        public ErrorDetails(int errorCode, String errorMessage) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    public UploadResponse() {

    }

    public UploadResponse(String scanId, String fileName, String downloadUri,
                          String fileType, long size) {
        this.scanId = scanId;
        this.fileName = fileName;
        this.downloadUri = downloadUri;
        this.fileType = fileType;
        this.size = size;
    }

    public void addErrorDetails(int errorCode, String errorMessage) {
        if (null == errorDetails) {
            errorDetails = new ArrayList<>();
        }
        this.errorDetails.add(new ErrorDetails(errorCode, errorMessage));
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDownloadUri() {
        return downloadUri;
    }

    public void setDownloadUri(String downloadUri) {
        this.downloadUri = downloadUri;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public List<ErrorDetails> getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(List<ErrorDetails> errorDetails) {
        this.errorDetails = errorDetails;
    }

    public String getScanId() {
        return scanId;
    }

    public void setScanId(String scanId) {
        this.scanId = scanId;
    }
}
