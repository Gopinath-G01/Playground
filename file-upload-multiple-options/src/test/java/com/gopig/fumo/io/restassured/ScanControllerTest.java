package com.gopig.fumo.io.restassured;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ScanControllerTest {

	@LocalServerPort
	private int randomPort;
	
	@Before
	public void setup() throws Exception {
		RestAssured.baseURI = "http://"+InetAddress.getLocalHost().getHostName();
		RestAssured.port = randomPort;
		RestAssured.basePath = "/app/v1/upload";
	}
	
	protected RequestSpecification givenBaseSpec() {
		try {
			return RestAssured.given().log().all()
					.baseUri("http://"+InetAddress.getLocalHost().getHostName())
					.port(randomPort)
					.basePath("/app/v1/upload");
		}catch(UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Test
    public void single_fileUpload_db_success_test() {
		givenBaseSpec()
			.formParam("scanInfo", "{\"patientId\":\"patient_101\", \"scanRecommendedBy\":\"Dr.ABC\", \"scanSummary\":\"All vitals are normal. No possible trace of diabetics.\" } ")
            .multiPart("files", new File("src/test/resources/scanInfo.json"))
            .accept(ContentType.JSON)
        .when()
            .post("/db")
        .then().log().all()
        	.assertThat()
            	.statusCode(200)
            .and()
            .assertThat()
            	.body("result", equalTo("Upload Successful"))
            	.body("scanId", notNullValue())
            ;
    }
	
	@Test
    public void single_fileUpload_cloud_failure_test() {
		givenBaseSpec()
			.formParam("scanInfo", "{\"patientId\":\"patient_101\", \"scanRecommendedBy\":\"Dr.ABC\", \"scanSummary\":\"All vitals are normal. No possible trace of diabetics.\" } ")
			.multiPart("files", new File("src/test/resources/scanInfo.json"))
            .accept(ContentType.JSON)
        .when()
            .post("/cloud")
        .then()
        	.assertThat()
            	.statusCode(200)
            .and()
            .assertThat()
            	.body("result", equalTo("Upload Successful"))
            	.body("scanId", notNullValue())
            	.body("fileDetailsResponse.fileCloudUrl", nullValue())
            ;
    }
	
	@Test
    public void single_fileUpload_db_multipart_failure_exceedSize_test() {
		String response = givenBaseSpec()
			.formParam("scanInfo", "{\"patientId\":\"patient_101\", \"scanRecommendedBy\":\"Dr.ABC\", \"scanSummary\":\"All vitals are normal. No possible trace of diabetics.\" } ")
            .multiPart("files", new File("src/test/resources/Test_Video.MP4"))
            .accept(ContentType.JSON)
        .when()
            .post("/db")
        .then().log().all()
        	.assertThat()
            	.statusCode(500)
            .and()
            .extract().body().asString()
            ;
		System.out.println("OUTPUT: "+response);
		Assert.assertTrue(response.contains("The field files exceeds its maximum permitted size of 2097152 bytes"));
    }
	
	@Test
    public void single_fileUpload_db_failure_missingFile_test() {
    	givenBaseSpec()
			.formParam("scanInfo", "{\"patientId\":\"patient_101\", \"scanRecommendedBy\":\"Dr.ABC\", \"scanSummary\":\"All vitals are normal. No possible trace of diabetics.\" } ")
	        .accept(ContentType.JSON)
        .when()
            .post("/db")
        .then()
        	.assertThat()
            	.statusCode(500)
            ;
    }
}
