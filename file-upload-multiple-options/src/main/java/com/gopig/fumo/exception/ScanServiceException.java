package com.gopig.fumo.exception;

import java.util.List;

public class ScanServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final ScanServiceError scanServiceError;

	/**
	 * Default constructor to create ServiceException
	 * with errorCode and errorMessage
	 * 
	 * @param errorCode
	 * @param errorMessage
	 */
	public ScanServiceException(String errorCode, String errorMessage) {
		super();
		this.scanServiceError = createScanServiceError(errorCode,errorMessage);
	}
	
	/**
	 * Constructor to create to create ServiceException
	 * with errorCode and errorMessage along with 
	 * cause of the Exception
	 * 
	 * @param errorCode
	 * @param errorMessage
	 * @param cause
	 */
	public ScanServiceException(String errorCode, String errorMessage,Throwable cause) {
		super(cause);
		this.scanServiceError = createScanServiceError(errorCode,errorMessage);
	}

	/**
	 * Constructor to create to create ServiceException
	 * with errorCode and errorMessage along with 
	 * message & cause of the Exception
	 * 
	 * @param errorCode
	 * @param errorMessage
	 * @param excMsg
	 * @param cause
	 */
	public ScanServiceException(String errorCode, String errorMessage, String excMsg, Throwable cause) {
		super(excMsg,cause);
		this.scanServiceError = createScanServiceError(errorCode,errorMessage);
	}
	
	/**
	 * Create the POJO representation of error
	 * 
	 * @param errorCode
	 * @param errorMessage
	 * @return ScanServiceError
	 */
	private ScanServiceError createScanServiceError(String errorCode, String errorMessage) {
		
		return new ScanServiceError(errorCode,errorMessage);
	}

	/**
	 * Method to add a detail about the Exception
	 * 
	 * @param errorCode
	 * @param errorMessage
	 * @return ScanServiceException
	 */
	public ScanServiceException addErrorDetail(String errorCode,String errorMessage) {
		this.getScanServiceError().addErrorDetail(errorCode, errorMessage);
		return this;
	}
	
	/**
	 * Method to add more details about the Exception
	 * 
	 * @param details
	 * @return ScanServiceException
	 */
	public ScanServiceException addErrorDetails(List<ScanServiceError.ErrorDetail> details) {
		if(null != details) {
			details.forEach(errorDetail -> this.getScanServiceError().addErrorDetail(errorDetail));
		}
		return this;
	}

	public ScanServiceError getScanServiceError() {
		return scanServiceError;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
