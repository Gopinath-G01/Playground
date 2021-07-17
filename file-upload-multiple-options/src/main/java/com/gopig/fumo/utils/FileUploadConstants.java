package com.gopig.fumo.utils;

public class FileUploadConstants {

	private FileUploadConstants() {
		//Not a class to instantiate
	}

	public static final String DESTINATION_DB = "db";
	public static final String DESTINATION_LOCAL_FILESYSTEM = "localFS";
	public static final String DESTINATION_CLOUD = "cloud";
	
	public static final String FILE_NAME_PREFIX = "UPLOAD_";
	public static final String DEVICE_ID_PREFIX = "DEVICE_";
	
	public static final String UPLOAD_RESULT_SUCCESS = "Upload Successful";
	public static final String UPLOAD_RESULT_FAILED = "Upload Failed";
}
