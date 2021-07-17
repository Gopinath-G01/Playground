package com.gopig.fumo.model.entities;

import java.time.LocalDateTime;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedBy;

@Entity
@Table(name = "SCAN_DETAILS")
public class ScanDetailsEntity {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String scanId;

	/* Upload MetaData */
	private String deviceId;
	private String patientId;
	private String scanRecommendedBy;
	private String scanSummary;

	/* Date & Time */
	@Column(name = "INSERTED_DATE")
	@CreationTimestamp
	private LocalDateTime insertedDate;

	@Column(name = "INSERTED_BY")
	@LastModifiedBy
	private String insertedBy;

	@Column(name = "LAST_UPDATED_DATE")
	@UpdateTimestamp
	private LocalDateTime lastUpdatedDate;

	@Column(name = "LAST_UPDATED_BY")
	@LastModifiedBy
	private String lastUpdatedBy;

	@OneToMany(mappedBy = "scanDetails")
	private Collection<FileStorageDetailsEntity> uploadDetails;

	/* Getters & Setters */
	public String getScanId() {
		return scanId;
	}

	public void setScanId(String scanId) {
		this.scanId = scanId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getScanRecommendedBy() {
		return scanRecommendedBy;
	}

	public void setScanRecommendedBy(String scanRecommendedBy) {
		this.scanRecommendedBy = scanRecommendedBy;
	}

	public String getScanSummary() {
		return scanSummary;
	}

	public void setScanSummary(String scanSummary) {
		this.scanSummary = scanSummary;
	}

	public LocalDateTime getInsertedDate() {
		return insertedDate;
	}

	public void setInsertedDate(LocalDateTime insertedDate) {
		this.insertedDate = insertedDate;
	}

	public String getInsertedBy() {
		return insertedBy;
	}

	public void setInsertedBy(String insertedBy) {
		this.insertedBy = insertedBy;
	}

	public LocalDateTime getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Collection<FileStorageDetailsEntity> getUploadDetails() {
		return uploadDetails;
	}

	public void setUploadDetails(Collection<FileStorageDetailsEntity> uploadDetails) {
		this.uploadDetails = uploadDetails;
	}

}
