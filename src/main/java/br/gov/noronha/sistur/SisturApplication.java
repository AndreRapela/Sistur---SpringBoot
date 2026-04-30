package br.gov.noronha.sistur;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@org.springframework.cache.annotation.EnableCaching
@SpringBootApplication
@ComponentScan(basePackages = "br.gov.noronha.sistur")
@EnableJpaRepositories(basePackages = "br.gov.noronha.sistur")
@EntityScan(basePackages = "br.gov.noronha.sistur")
public class SisturApplication {

	public static void main(String[] args) {
		SpringApplication.run(SisturApplication.class, args);
	}

}
