package com.igrecsys.proputil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PropertyUtilitiesApplication {

    private int maxUploadSizeInMb = 200 * 1024 * 1024; // 10 MB

	public static void main(String[] args) {
		SpringApplication.run(PropertyUtilitiesApplication.class, args);

    }



}
