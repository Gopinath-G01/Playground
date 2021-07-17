package com.gopig.fumo.validator;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.gopig.fumo.model.request.ScanInfo;
import com.gopig.fumo.model.validator.ValidationResult;
import com.gopig.fumo.validator.InputValidator;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class InputValidatorTest {

    @InjectMocks
    InputValidator inputValidator;
    ScanInfo scanInfo;

    @BeforeEach
    public void init() {
    	scanInfo = new ScanInfo();
    	scanInfo.setDeviceId("DEVICE_101");
    	scanInfo.setPatientId("Patient_101");
    	scanInfo.setScanRecommendedBy("Dr.ABC");
    	scanInfo.setScanSummary("Summary look good");
    }

    @Test
    public void scanInfo_success_Test() {
        ValidationResult validationResult = inputValidator.validate(scanInfo);
        Assert.assertEquals("0", validationResult.getCode());
    }
    
    @Test
    public void scanInfoNullTest() {
    	scanInfo = null;
        ValidationResult validationResult = inputValidator.validate(scanInfo);
        Assert.assertEquals("500", validationResult.getCode());
    }
    
    @Test
    public void scanInfo_WithoutPatientId_Test() {
    	scanInfo.setPatientId(null);
        ValidationResult validationResult = inputValidator.validate(scanInfo);
        Assert.assertEquals("502", validationResult.getCode());
    }
}
