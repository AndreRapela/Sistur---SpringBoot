package br.gov.noronha.sistur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@org.springframework.cache.annotation.EnableCaching
@SpringBootApplication
public class SisturApplication {

	public static void main(String[] args) {
		SpringApplication.run(SisturApplication.class, args);
	}

}
