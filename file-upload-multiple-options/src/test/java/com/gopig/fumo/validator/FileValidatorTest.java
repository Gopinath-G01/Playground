package com.gopig.fumo.validator;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.gopig.fumo.model.validator.ValidationResult;
import com.gopig.fumo.validator.FileValidator;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class FileValidatorTest {

    @InjectMocks
    FileValidator fileValidator;
    MockMultipartFile NO_FILE;
    MockMultipartFile jpegFile;
    MockMultipartFile pdfFile;
    
    @BeforeEach
    public void init() {
    	jpegFile = new MockMultipartFile("jpegFile", "image01.jpeg", MediaType.IMAGE_JPEG_VALUE, "imageinBytes".getBytes());
    	pdfFile = new MockMultipartFile("pdfFile", "pdf01.pdf", MediaType.APPLICATION_PDF_VALUE, "pdfInBytes".getBytes());
    }

    @Test
    public void singleFile_success_Test() {
        ValidationResult validationResult = fileValidator.validate(jpegFile);
        Assert.assertEquals("0", validationResult.getCode());
    }
    
    @Test
    public void singleFile_EmptyFile_Test() {
    	jpegFile = new MockMultipartFile("jpegFile", "".getBytes());
        ValidationResult validationResult = fileValidator.validate(jpegFile);
        Assert.assertEquals("400", validationResult.getCode());
    }
    
    @Test
    public void singleFile_NoFile_Test() {
        ValidationResult validationResult = fileValidator.validate(NO_FILE);
        Assert.assertEquals("400", validationResult.getCode());
    }
    
    @Test
    public void multiFile_success_Test() {
        List<ValidationResult> validationResult = fileValidator.validate(new MultipartFile[] {jpegFile,pdfFile});
        validationResult.forEach(result -> {Assert.assertEquals("0", result.getCode());});
    }
    
    @Test
    public void multiFile_NoFile_Test() {
        List<ValidationResult> validationResult = fileValidator.validate(new MultipartFile[] {NO_FILE});
        validationResult.forEach(result -> {Assert.assertEquals("400", result.getCode());});
    }
}
