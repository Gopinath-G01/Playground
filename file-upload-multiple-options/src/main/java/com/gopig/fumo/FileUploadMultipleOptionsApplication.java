package com.gopig.fumo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages="com")
@EnableJpaRepositories(basePackages = "com.gopig.fumo.repository")
@EntityScan(basePackages = "com.gopig.fumo.model")
public class FileUploadMultipleOptionsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileUploadMultipleOptionsApplication.class, args);
	}

}
