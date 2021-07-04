package com.gopig.fumo.validator;

import com.gopig.fumo.model.validator.ValidationResult;
import com.gopig.fumo.model.validator.ValidationStatus;

public interface PortalValidator<T> {
    ValidationResult validate(T data);
    default ValidationResult constructResult(ValidationStatus status) {
        return new ValidationResult(status);
    }
}
