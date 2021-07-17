package com.gopig.fumo.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FILE_STORAGE_DETAILS")
public class FileStorageDetailsEntity {
	@Id
	@GeneratedValue(generator = "uuid", strategy = GenerationType.IDENTITY)
	private String fileId;

	/* File Related */
	@Lob
	private byte[] rawContent;
	private String fileType;
	private String fileName;
	private long fileSize;

	/* Cloud Related */
	private String fileCloudURL;
	private boolean isSyncedWithCloud;
	
	@ManyToOne
	@JoinColumn(name = "scanId")
	private ScanDetailsEntity scanDetails;

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public byte[] getRawContent() {
		return rawContent;
	}

	public void setRawContent(byte[] rawContent) {
		this.rawContent = rawContent;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public ScanDetailsEntity getScanDetails() {
		return scanDetails;
	}

	public void setScanDetails(ScanDetailsEntity scanDetails) {
		this.scanDetails = scanDetails;
	}

	public String getFileCloudURL() {
		return fileCloudURL;
	}

	public void setFileCloudURL(String fileCloudURL) {
		this.fileCloudURL = fileCloudURL;
	}

	public boolean isSyncedWithCloud() {
		return isSyncedWithCloud;
	}

	public void setSyncedWithCloud(boolean isSyncedWithCloud) {
		this.isSyncedWithCloud = isSyncedWithCloud;
	}

}
