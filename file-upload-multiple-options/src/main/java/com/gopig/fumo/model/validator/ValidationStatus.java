package com.gopig.fumo.model.validator;

public enum ValidationStatus {
    SUCCESS("0", "Successful validation."),
    FILE_INPUT_ERROR("400", "No file provided."),
    FILE_NAME_UNSUPPORTED("401", "Unsupported file name - "),

    MISSING_POST_REQUEST_PARAMS("500", "Input error: Either scanInfo or file(s) paramter is missing in the request."),
    MISSING_GET_REQUEST_PARAMS("501", "Input error: Id is either missing or invalid."),
    MISSING_PATIENT_ID("502", "Input error: missing mandatory param : {patientId}.");

    private final String value;
    private String message;

    private ValidationStatus(String value, String message) {
        this.value = value;
        this.message = message;
    }

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public ValidationStatus addToMessage(String message) {
        this.message += message;
        return this;
    }
}
