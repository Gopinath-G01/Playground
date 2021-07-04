package com.gopig.fumo.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ScanUploadExceptionHandler {

	// @ExceptionHandler
	/*
	 * public UploadResponse handleServiceException(ServiceException
	 * serviceException) { UploadResponse uploadResponse = new UploadResponse();
	 * uploadResponse.addErrorDetails(serviceException.getCode(),
	 * serviceException.getMessage()); }
	 */

	@ExceptionHandler
	public String handleException(Exception exception, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {

		String defaultMsg = "File Upload/Download Failed. Please try again";
		String customErrorMsg = "";
		if (exception.getClass().getName().contains("MissingServletRequestParameterException")
				|| exception.getClass().getName().contains("MissingServletRequestPartException")) {
			customErrorMsg = "Either scanInfo or file(s) paramter is missing in the request. Please check and try again.";
		} else if (exception.getClass().getName().contains("MethodArgumentConversionNotSupportedException")) {
			customErrorMsg = "Please check dataTypes of the params are correct [files -> MultiPartFile, scanInfo -> Json in String format]";
		} else if (exception.getClass().getName().contains("MissingPathVariableException")
				|| exception.getClass().getName().contains("EntityNotFoundException")) {
			customErrorMsg = "Please provide a valid \'Id\' for downloading the file. Please refer the upload response to get the ScanId";
		} else if (exception.getClass().getName().contains("ScanServiceException")) {
			customErrorMsg = ((ScanServiceException)exception).getErrMessage();
		} else {
			customErrorMsg = defaultMsg;
		}

		return customErrorMsg;
	}
}
