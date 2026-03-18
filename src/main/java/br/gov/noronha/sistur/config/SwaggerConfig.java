package br.gov.noronha.sistur.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.*;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI sisturOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SisTur API - Fernando de Noronha")
                        .description("API oficial para o Sistema de Turismo de Fernando de Noronha.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Suporte Noronha")
                                .email("suporte@noronha.gov.br"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
