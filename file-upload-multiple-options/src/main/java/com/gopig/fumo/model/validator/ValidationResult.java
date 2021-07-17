package com.gopig.fumo.model.validator;

public class ValidationResult {
    private String code;
    private String message;

    public ValidationResult() {

    }

    public ValidationResult(ValidationStatus status) {
        this.code = status.getValue();
        this.message = status.getMessage();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
