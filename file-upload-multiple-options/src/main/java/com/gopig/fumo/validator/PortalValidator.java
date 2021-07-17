package com.gopig.fumo.validator;

import java.util.ArrayList;
import java.util.List;

import com.gopig.fumo.model.validator.ValidationResult;
import com.gopig.fumo.model.validator.ValidationStatus;

public interface PortalValidator<T> {
    ValidationResult validate(T data);

    default List<ValidationResult> validate(T[] data) {
        return new ArrayList<>();
    }

    default ValidationResult constructResult(ValidationStatus status) {
        return new ValidationResult(status);
    }
}
