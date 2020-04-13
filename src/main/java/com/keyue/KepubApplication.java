package com.keyue;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations = { "classpath:context/applicationContext.xml" })
public class KepubApplication {

	public static void main(String[] args) {
		SpringApplication.run(KepubApplication.class, args);
	}

}
