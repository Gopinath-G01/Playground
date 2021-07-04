package com.gopig.fumo.model.validator;

public class ValidationResult {
    private int code;
    private String message;

    public ValidationResult() {

    }

    public ValidationResult(ValidationStatus status) {
        this.code = status.getValue();
        this.message = status.getMessage();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
