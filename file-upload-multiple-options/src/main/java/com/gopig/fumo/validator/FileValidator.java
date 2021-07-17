package com.gopig.fumo.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.gopig.fumo.model.validator.ValidationResult;
import com.gopig.fumo.model.validator.ValidationStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component("fileValidator")
public class FileValidator implements PortalValidator<MultipartFile> {
    private static final Logger log = LoggerFactory.getLogger(FileValidator.class);

    @Override
    public List<ValidationResult> validate(MultipartFile[] files) {
        List<ValidationResult> response = new ArrayList<>();
        if (null == files || files.length == 0) {
            log.warn("No file provided to validate, requesting retry.");
            response.add(constructResult(ValidationStatus.FILE_INPUT_ERROR));
        } else {
            Arrays.stream(files).forEach(
                    file ->
                            response.add(validate(file))
            );
        }
        return response;
    }

    @Override
    public ValidationResult validate(MultipartFile file) {
        ValidationStatus validationStatus = ValidationStatus.SUCCESS;
        if (null == file || file.isEmpty() ||
                org.apache.commons.lang3.StringUtils.isBlank(file.getOriginalFilename())) {
            log.warn("No file provided to validate, requesting retry.");
            validationStatus = ValidationStatus.FILE_INPUT_ERROR;
        } else {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            log.info("Initiating validation for file: {}", fileName);
            /* Check if the file name contains invalid characters */
            if (org.apache.commons.lang3.StringUtils.isNotBlank(fileName) &&
                    fileName.contains("..")) {
                validationStatus = ValidationStatus.FILE_NAME_UNSUPPORTED.addToMessage(fileName);
            }
        }
        return constructResult(validationStatus);
    }
}
