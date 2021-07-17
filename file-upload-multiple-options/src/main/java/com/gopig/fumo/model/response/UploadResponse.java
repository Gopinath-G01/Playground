package com.gopig.fumo.model.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UploadResponse {
    private String scanId;
    private String result;
    private String downloadUri;
    private List<FileDetailsResponse> fileDetails;

    private class FileDetailsResponse {
    	
    	private String fileId;
		private String fileName;
        private String fileCloudUrl;
        private String fileType;
        private long size;
        
        public FileDetailsResponse(String fileId, String fileName, String fileType, long size, String fileCloudUrl) {
			this.setFileId(fileId);
        	this.fileName = fileName;
			this.fileType = fileType;
			this.size = size;
			this.fileCloudUrl = fileCloudUrl;
		}
        
		public String getFileId() {
			return fileId;
		}

		public void setFileId(String fileId) {
			this.fileId = fileId;
		}

		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public String getFileCloudUrl() {
			return fileCloudUrl;
		}
		public void setFileCloudUrl(String fileCloudUrl) {
			this.fileCloudUrl = fileCloudUrl;
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
        
    }
    public UploadResponse() {

    }

    public void addFileDetails(String fileId, String fileName, String fileType, long size, String fileCloudUrl) {
        if (null == fileDetails) {
        	fileDetails = new ArrayList<>();
        }
        this.fileDetails.add(new FileDetailsResponse(fileId, fileName, fileType, size, fileCloudUrl));
    }

    public List<FileDetailsResponse> getFileDetailsResponseList() {
        return fileDetails;
    }

    public void setFileDetailsResponseList(List<FileDetailsResponse> fileDetails) {
        this.fileDetails = fileDetails;
    }

    public String getScanId() {
        return scanId;
    }

    public void setScanId(String scanId) {
        this.scanId = scanId;
    }

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getDownloadUri() {
		return downloadUri;
	}

	public void setDownloadUri(String downloadUri) {
		this.downloadUri = downloadUri;
	}
}
