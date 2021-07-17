package com.gopig.fumo.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(value = Include.NON_EMPTY)
public class ScanServiceError implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;
	private String message;
	private List<ErrorDetail> errorDetails;

	public ScanServiceError(String errCode,String errMessage) {
		super();
		this.code = errCode;
		this.message = errMessage;
	}

	public String getErrCode() {
		return code;
	}

	public void setErrCode(String errCode) {
		this.code = errCode;
	}

	public String getErrMessage() {
		return message;
	}

	public void setErrMessage(String errMessage) {
		this.message = errMessage;
	}

	public List<ErrorDetail> getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(List<ErrorDetail> errorDetails) {
		this.errorDetails = errorDetails;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void addErrorDetail(String errorCode, String errorMessage) {
		this.addErrorDetail(new ErrorDetail(errorCode, errorMessage));
	}
	
	public void addErrorDetail(ErrorDetail errorDetail) {
		if (null == errorDetails) {
			this.errorDetails = new ArrayList<>();
		}
		this.errorDetails.add(errorDetail);
	}
	
	static class ErrorDetail implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private String errorCode;
		private String errorMessage;
		
		public ErrorDetail(String errorCode, String errorMessage) {
			super();
			this.setErrorCode(errorCode);
			this.setErrorMessage(errorMessage);
		}

		public String getErrorCode() {
			return errorCode;
		}

		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}

		@JsonProperty("description")
		public String getErrorMessage() {
			return errorMessage;
		}

		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}
	}

}
