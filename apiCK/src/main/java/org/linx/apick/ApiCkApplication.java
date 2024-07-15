package org.linx.apick;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ApiCkApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiCkApplication.class, args);
	}

}
