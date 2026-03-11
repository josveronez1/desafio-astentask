package com.josveronez.desafio_astentask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class DesafioAstentaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioAstentaskApplication.class, args);
	}

}
