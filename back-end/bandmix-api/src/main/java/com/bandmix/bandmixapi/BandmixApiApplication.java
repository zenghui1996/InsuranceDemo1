package com.bandmix.bandmixapi;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringCloudApplication
@EnableFeignClients
@EnableJpaAuditing
public class BandmixApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BandmixApiApplication.class, args);
	}

}
