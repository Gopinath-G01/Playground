package com.gopig.fumo.validator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gopig.fumo.model.request.ScanInfo;
import com.gopig.fumo.model.validator.ValidationResult;
import com.gopig.fumo.model.validator.ValidationStatus;

@Component("inputValidator")
public class InputValidator implements PortalValidator<ScanInfo> {
    private static final Logger log = LoggerFactory.getLogger(InputValidator.class);

    @Override
    public ValidationResult validate(ScanInfo scanInfo) {
        ValidationStatus validationStatus = ValidationStatus.SUCCESS;
        if (null == scanInfo) {
            validationStatus = ValidationStatus.MISSING_POST_REQUEST_PARAMS;
        } else if (StringUtils.isBlank(scanInfo.getPatientId())) {
            validationStatus = ValidationStatus.MISSING_PATIENT_ID;
        }
        log.info("Validation Result: {}", validationStatus.getMessage());
        return constructResult(validationStatus);
    }
}