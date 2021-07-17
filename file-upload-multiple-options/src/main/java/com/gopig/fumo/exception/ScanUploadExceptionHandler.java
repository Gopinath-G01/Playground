package com.gopig.fumo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ScanUploadExceptionHandler {

//	@ExceptionHandler
//	public ResponseEntity<ScanServiceException> handleException(ScanServiceException scanServiceException, HttpServletRequest httpRequest,
//			HttpServletResponse httpResponse) {
//		return ResponseEntity.internalServerError().body(scanServiceException);
//	}

	@ExceptionHandler(ScanServiceException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ScanServiceError> handleCustomException(ScanServiceException scanServiceException) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(scanServiceException.getScanServiceError());
	}

//	@ExceptionHandler
//	public String handleException(Exception exception, HttpServletRequest httpRequest,
//			HttpServletResponse httpResponse) {
//
//		String defaultMsg = "File Upload/Download Failed. Please try again";
//		String customErrorMsg = "";
//		if (exception.getClass().getName().contains("MissingServletRequestParameterException")
//				|| exception.getClass().getName().contains("MissingServletRequestPartException")) {
//			customErrorMsg = "Either scanInfo or file(s) paramter is missing in the request. Please check and try again.";
//		} else if (exception.getClass().getName().contains("MethodArgumentConversionNotSupportedException")) {
//			customErrorMsg = "Please check dataTypes of the params are correct [files -> MultiPartFile, scanInfo -> Json in String format]";
//		} else if (exception.getClass().getName().contains("MissingPathVariableException")
//				|| exception.getClass().getName().contains("EntityNotFoundException")) {
//			customErrorMsg = "Please provide a valid \'Id\' for downloading the file. Please refer the upload response to get the ScanId";
//		} else if (exception.getClass().getName().contains("ScanServiceException")) {
//			customErrorMsg = ((ScanServiceException)exception).getErrMessage();
//		} else {
//			customErrorMsg = defaultMsg;
//		}
//
//		return customErrorMsg;
//	}
}
