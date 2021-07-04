package com.gopig.fumo.model.request;

public class ScanInfo {
    private String deviceId;
    private String patientId;
    private String scanId;
    private String scanRecommendedBy;
    private String scanSummary;

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

    public String getScanId() {
        return scanId;
    }

    public void setScanId(String scanId) {
        this.scanId = scanId;
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
}
