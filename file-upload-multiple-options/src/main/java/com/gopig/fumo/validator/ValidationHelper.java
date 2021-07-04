package com.gopig.fumo.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.gopig.fumo.model.request.ScanInfo;
import com.gopig.fumo.model.validator.ValidationResult;

@Component
public class ValidationHelper {

    @Autowired
    @Qualifier("fileValidator")
    PortalValidator<MultipartFile> fileValidator;

    @Autowired
    @Qualifier("inputValidator")
    PortalValidator<ScanInfo> scanInfoPortalValidator;

    public List<ValidationResult> validateUpload(ScanInfo scanInfo, MultipartFile file) {
        List<ValidationResult> response = new ArrayList<>();
        response.add(fileValidator.validate(file));
        response.add(scanInfoPortalValidator.validate(scanInfo));
        return response;
    }
}
