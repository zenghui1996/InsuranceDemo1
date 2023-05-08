package com.bandmix.oauthapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class OauthApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OauthApiApplication.class, args);
	}

}
