package com.webservice.rest;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@Configuration
@EnableConfigurationProperties({
    FileStorageProperties.class
})
public class RestApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApplication.class, args);
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setBufferRequestBody(false);

		RestTemplate template = new RestTemplate(factory);
	}

}
